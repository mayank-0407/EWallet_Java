package com.ewallet.refund_service.exceptions;

public class RefundProcessingException extends RuntimeException {
    public RefundProcessingException(String message) {
        super(message);
    }
}