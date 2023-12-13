package me.eokasta.appvirtualbank.transaction.exception;

import lombok.Getter;

@Getter
public class UserNotAuthenticatedException extends RuntimeException {

    private final String authenticatedCpf;
    private final String senderCpf;

    public UserNotAuthenticatedException(String message, String authenticatedCpf, String senderCpf) {
        super(message);
        this.authenticatedCpf = authenticatedCpf;
        this.senderCpf = senderCpf;
    }

}
