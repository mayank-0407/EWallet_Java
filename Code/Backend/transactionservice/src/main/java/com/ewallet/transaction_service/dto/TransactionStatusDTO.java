package com.ewallet.transaction_service.dto;

import com.ewallet.transaction_service.enums.TransactionStatus;
import lombok.Data;

@Data
public class TransactionStatusDTO {
    private Long transactionId;
    private TransactionStatus status;

    public TransactionStatusDTO() {
    }

    public TransactionStatusDTO(Long transactionId, TransactionStatus status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    @Override
    public String toString() {
        return "TransactionStatusDto{" +
                "transactionId=" + transactionId +
                ", status=" + status +
                '}';
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
