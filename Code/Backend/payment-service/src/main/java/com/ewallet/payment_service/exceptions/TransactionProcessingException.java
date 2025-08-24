package com.ewallet.payment_service.exceptions;

public class TransactionProcessingException extends RuntimeException {
    public TransactionProcessingException(String message) {
        super(message);
    }
}