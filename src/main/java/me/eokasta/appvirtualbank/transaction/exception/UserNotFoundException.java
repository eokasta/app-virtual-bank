package me.eokasta.appvirtualbank.transaction.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String cpf;

    public UserNotFoundException(String message, String cpf) {
        super(message);

        this.cpf = cpf;
    }

}
