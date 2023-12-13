package me.eokasta.appvirtualbank.transaction.dto;

import me.eokasta.appvirtualbank.transaction.exception.UserNotAuthenticatedException;

public record UserNotAuthenticatedResponseDTO(
        String message,
        String authenticatedCpf,
        String senderCpf
) {

    public static UserNotAuthenticatedResponseDTO from(UserNotAuthenticatedException exception) {
        return new UserNotAuthenticatedResponseDTO(
                exception.getMessage(),
                exception.getAuthenticatedCpf(),
                exception.getSenderCpf());
    }

}
