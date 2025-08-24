package com.ewallet.transaction_service.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private String senderId;
    private String receiverId;
    private Double amount;
    private String remarks;

    public TransactionDTO() {
    }

    public TransactionDTO(String senderId, String receiverId, Double amount, String remarks) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
