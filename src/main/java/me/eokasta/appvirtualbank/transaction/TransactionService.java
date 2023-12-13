package me.eokasta.appvirtualbank.transaction;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import me.eokasta.appvirtualbank.transaction.dto.DepositTransactionDTO;
import me.eokasta.appvirtualbank.transaction.dto.TransactionResponseDTO;
import me.eokasta.appvirtualbank.transaction.exception.*;
import me.eokasta.appvirtualbank.user.User;
import me.eokasta.appvirtualbank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(
            TransactionRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = TransactionErrorException.class)
    public Pair<TransactionResponseDTO, TransactionResponseDTO> deposit(DepositTransactionDTO data) {
        final String senderCpf = data.cpfSender();
        final User userSender = userRepository.findByCpf(senderCpf);
        final User userReceiver = userRepository.findByCpf(data.cpfReceiver());

        validate(data, userSender, userReceiver);

        final BigDecimal senderOldBalance = userSender.getBalance();
        final BigDecimal receiverOldBalance = userReceiver.getBalance();
        if (senderOldBalance.compareTo(data.value()) < 0)
            throw new InsufficientBalanceException("Saldo insuficiente.", senderCpf, senderOldBalance, data.value());

        final BigDecimal senderNewBalance = senderOldBalance.subtract(data.value());
        final BigDecimal receiverNewBalance = receiverOldBalance.add(data.value());
        userSender.setBalance(senderNewBalance);
        userReceiver.setBalance(receiverNewBalance);

        userRepository.save(userSender);
        userRepository.save(userReceiver);

        final Transaction senderTransaction = new Transaction(
                null,
                userSender.getId(),
                data.value(),
                Instant.now(),
                TransactionType.WITHDRAW,
                senderOldBalance,
                senderNewBalance
        );

        final Transaction receiverTransaction = new Transaction(
                null,
                userReceiver.getId(),
                data.value(),
                Instant.now(),
                TransactionType.DEPOSIT,
                receiverOldBalance,
                receiverNewBalance
        );

        final TransactionResponseDTO senderResponse =
                registerTransaction(repository, senderTransaction, userSender.getCpf());
        final TransactionResponseDTO receiverResponse =
                registerTransaction(repository, receiverTransaction, userReceiver.getCpf());

        return Pair.of(senderResponse, receiverResponse);
    }

    private void validate(@NotNull DepositTransactionDTO data, @Nullable User userSender, @Nullable User userReceiver) {
        if (userSender == null)
            throw new UserNotFoundException("CPF do remetente não encontrado.", data.cpfSender());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User authenticatedUser = (User) authentication.getPrincipal();

        final String authenticatedUserCpf = authenticatedUser.getCpf();
        if (!authenticatedUserCpf.equals(userSender.getCpf()))
            throw new UserNotAuthenticatedException(
                    "Usuário autenticado não é o remetente.",
                    authenticatedUserCpf,
                    userSender.getCpf());

        if (userReceiver == null)
            throw new UserNotFoundException("CPF do destinatário não encontrado.", data.cpfReceiver());

        if (userSender.getId().equals(userReceiver.getId()))
            throw new SelfTransferException("Não é possível transferir para você mesmo.");
    }

    private TransactionResponseDTO registerTransaction(
            TransactionRepository repository,
            Transaction transaction,
            String cpf
    ) {
        repository.save(transaction);
        return TransactionResponseDTO.fromTransaction(transaction, cpf);
    }

}
