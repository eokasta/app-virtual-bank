package me.eokasta.appvirtualbank.transaction;

import jakarta.validation.Valid;
import me.eokasta.appvirtualbank.user.User;
import me.eokasta.appvirtualbank.user.UserRepository;
import me.eokasta.appvirtualbank.utils.MessageResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionController(
            TransactionRepository repository,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PutMapping
    public ResponseEntity deposit(@RequestBody @Valid DepositTransactionDTO data) {
        final User userSender = userRepository.findByCpf(data.cpfSender());
        final User userReceiver = userRepository.findByCpf(data.cpfReceiver());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User authenticatedUser = (User) authentication.getPrincipal();
        if (!authenticatedUser.getCpf().equals(data.cpfSender()))
            return ResponseEntity.status(401).body(new MessageResponseDTO("Usuário autenticado não é o remetente."));

        if (userSender == null)
            return ResponseEntity.status(404).body(new MessageResponseDTO("CPF do remetente não encontrado."));

        if (userReceiver == null)
            return ResponseEntity.status(404).body(new MessageResponseDTO("CPF do destinatário não encontrado."));

        final BigDecimal senderOldBalance = userSender.getBalance();
        final BigDecimal receiverOldBalance = userReceiver.getBalance();
        if (senderOldBalance.compareTo(data.value()) < 0)
            return ResponseEntity.status(400).body(new MessageResponseDTO("Saldo insuficiente."));

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

        return ResponseEntity.ok(Map.of(
                "sender_transaction", senderResponse,
                "receiver_transaction", receiverResponse));
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
