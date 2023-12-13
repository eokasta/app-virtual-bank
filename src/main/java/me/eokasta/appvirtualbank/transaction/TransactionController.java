package me.eokasta.appvirtualbank.transaction;

import jakarta.validation.Valid;
import me.eokasta.appvirtualbank.transaction.dto.DepositTransactionDTO;
import me.eokasta.appvirtualbank.transaction.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(
            TransactionService service
    ) {
        this.service = service;
    }

    @PutMapping
    public ResponseEntity deposit(@RequestBody @Valid DepositTransactionDTO data) {
        final var responsePair = service.deposit(data);
        final TransactionResponseDTO senderResponse = responsePair.getFirst();
        final TransactionResponseDTO receiverResponse = responsePair.getSecond();

        return ResponseEntity.ok(Map.of(
                "sender_transaction", senderResponse,
                "receiver_transaction", receiverResponse));
    }

}
