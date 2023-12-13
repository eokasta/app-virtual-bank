package me.eokasta.appvirtualbank.transaction.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InsufficientBalanceException extends RuntimeException {

    private final String cpf;
    private final BigDecimal currentBalance;
    private final BigDecimal value;

    public InsufficientBalanceException(String message, String cpf, BigDecimal currentBalance, BigDecimal value) {
        super(message);

        this.cpf = cpf;
        this.currentBalance = currentBalance;
        this.value = value;
    }

}
