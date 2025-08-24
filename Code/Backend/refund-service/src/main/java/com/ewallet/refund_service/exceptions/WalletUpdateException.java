package com.ewallet.refund_service.exceptions;

public class WalletUpdateException extends RuntimeException {
    public WalletUpdateException(String message) {
        super(message);
    }
}