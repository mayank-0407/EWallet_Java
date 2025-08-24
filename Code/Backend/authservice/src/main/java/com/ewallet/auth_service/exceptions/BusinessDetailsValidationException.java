package com.ewallet.auth_service.exceptions;

public class BusinessDetailsValidationException extends RuntimeException {
    public BusinessDetailsValidationException(String message) {
        super(message);
    }
}