package com.ewallet.auth_service.exceptions;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String message) {
        super(message);
    }
}