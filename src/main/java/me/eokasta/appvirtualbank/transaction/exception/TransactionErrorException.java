package me.eokasta.appvirtualbank.transaction.exception;

public class TransactionErrorException extends RuntimeException {

    public TransactionErrorException(String message) {
        super(message);
    }

}
