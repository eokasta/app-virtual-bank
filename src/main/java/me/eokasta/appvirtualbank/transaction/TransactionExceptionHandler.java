package me.eokasta.appvirtualbank.transaction;

import me.eokasta.appvirtualbank.global.dto.MessageResponseDTO;
import me.eokasta.appvirtualbank.transaction.dto.InsufficientBalanceResponseDTO;
import me.eokasta.appvirtualbank.transaction.dto.UserNotAuthenticatedResponseDTO;
import me.eokasta.appvirtualbank.transaction.dto.UserNotFoundResponseDTO;
import me.eokasta.appvirtualbank.transaction.exception.InsufficientBalanceException;
import me.eokasta.appvirtualbank.transaction.exception.SelfTransferException;
import me.eokasta.appvirtualbank.transaction.exception.UserNotAuthenticatedException;
import me.eokasta.appvirtualbank.transaction.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity handleUserNotAuthenticatedException(UserNotAuthenticatedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserNotAuthenticatedResponseDTO.from(exception));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserNotFoundResponseDTO.from(exception));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity handleInsufficientBalanceException(InsufficientBalanceException exception) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(InsufficientBalanceResponseDTO.from(exception));
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity handleSelfTransferException(SelfTransferException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponseDTO(exception.getMessage()));
    }

}
