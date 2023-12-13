package me.eokasta.appvirtualbank.transaction.exception;

public class SelfTransferException extends RuntimeException {

    public SelfTransferException(String message) {
        super(message);
    }

}
