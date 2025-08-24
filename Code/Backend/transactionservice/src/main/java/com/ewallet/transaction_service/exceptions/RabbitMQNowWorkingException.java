package com.ewallet.transaction_service.exceptions;

public class RabbitMQNowWorkingException extends RuntimeException {
    public RabbitMQNowWorkingException(String message) {
        super(message);
    }
}
