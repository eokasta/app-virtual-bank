package me.eokasta.appvirtualbank.global.exception;

public record ErrorExceptionResponseDTO(
        String message,
        Exception exception
) {
}
