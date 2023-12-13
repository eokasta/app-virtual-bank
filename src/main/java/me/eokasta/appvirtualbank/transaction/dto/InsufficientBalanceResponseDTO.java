package me.eokasta.appvirtualbank.transaction.dto;

import me.eokasta.appvirtualbank.transaction.exception.InsufficientBalanceException;

import java.math.BigDecimal;

public record InsufficientBalanceResponseDTO(
        String message,
        String cpf,
        BigDecimal currentBalance,
        BigDecimal value
) {

    public static InsufficientBalanceResponseDTO from(InsufficientBalanceException exception) {
        return new InsufficientBalanceResponseDTO(
                exception.getMessage(),
                exception.getCpf(),
                exception.getCurrentBalance(),
                exception.getValue());
    }

}
