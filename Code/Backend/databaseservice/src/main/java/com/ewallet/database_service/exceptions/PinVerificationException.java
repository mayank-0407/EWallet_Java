package com.ewallet.database_service.exceptions;

public class PinVerificationException extends RuntimeException {
    public PinVerificationException(String message) {
        super(message);
    }
}