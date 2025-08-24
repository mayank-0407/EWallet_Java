package com.ewallet.wallet_service.exceptions;

public class PinVerificationException extends RuntimeException {

    public PinVerificationException(String message) {
        super(message);
    }

    public PinVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}