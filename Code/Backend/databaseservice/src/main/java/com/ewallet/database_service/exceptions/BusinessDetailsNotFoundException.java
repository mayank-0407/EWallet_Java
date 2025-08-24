package com.ewallet.database_service.exceptions;

public class BusinessDetailsNotFoundException extends RuntimeException {
    public BusinessDetailsNotFoundException(String message) {
        super(message);
    }
}