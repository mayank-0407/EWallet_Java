package com.ewallet.transaction_service.exceptions;

public class InvalidTransactionDetailsException extends RuntimeException {
    public InvalidTransactionDetailsException(String message) {
        super(message);
    }
}