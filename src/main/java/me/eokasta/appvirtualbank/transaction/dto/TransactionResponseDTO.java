package me.eokasta.appvirtualbank.transaction.dto;

import me.eokasta.appvirtualbank.transaction.Transaction;
import me.eokasta.appvirtualbank.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        Long id,
        Long accountId,
        String cpf,
        BigDecimal amount,
        Instant transactionDate,
        TransactionType transactionType,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {

    public static TransactionResponseDTO fromTransaction(Transaction transaction, String cpf) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAccountId(),
                cpf,
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getTransactionType(),
                transaction.getOldBalance(),
                transaction.getNewBalance()
        );
    }

}
