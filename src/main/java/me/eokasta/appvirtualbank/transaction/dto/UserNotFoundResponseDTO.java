package me.eokasta.appvirtualbank.transaction.dto;

import me.eokasta.appvirtualbank.transaction.exception.UserNotFoundException;

public record UserNotFoundResponseDTO(
        String message,
        String cpf
) {

    public static UserNotFoundResponseDTO from(UserNotFoundException exception) {
        return new UserNotFoundResponseDTO(exception.getMessage(), exception.getCpf());
    }

}
