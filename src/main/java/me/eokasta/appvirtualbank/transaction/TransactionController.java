package me.eokasta.appvirtualbank.transaction;

import jakarta.validation.Valid;
import me.eokasta.appvirtualbank.user.MessageResponseDTO;
import me.eokasta.appvirtualbank.user.User;
import me.eokasta.appvirtualbank.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionRepository repository;
    private UserRepository userRepository;

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

        if (userSender.getBalance().compareTo(data.value()) < 0)
            return ResponseEntity.status(400).body(new MessageResponseDTO("Saldo insuficiente."));

        userSender.setBalance(userSender.getBalance().subtract(data.value()));
        userReceiver.setBalance(userReceiver.getBalance().add(data.value()));

        userRepository.save(userSender);
        userRepository.save(userReceiver);

        final Transaction senderTransaction = new Transaction(
                null,
                userSender.getId(),
                data.value(),
                Instant.now(),
                TransactionType.WITHDRAW
        );

        final Transaction receiverTransaction = new Transaction(
                null,
                userReceiver.getId(),
                data.value(),
                Instant.now(),
                TransactionType.DEPOSIT
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
