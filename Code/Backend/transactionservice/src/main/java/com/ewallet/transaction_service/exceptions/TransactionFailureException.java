package com.ewallet.transaction_service.exceptions;

public class TransactionFailureException extends RuntimeException {
    public TransactionFailureException(String message) {
        super(message);
    }
}