package com.ewallet.transaction_service.models;

import com.ewallet.transaction_service.enums.TransactionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionModel {

    private Long id;
    private LocalDateTime transactionInitTime;

    private LocalDateTime transactionCompletedTime;

    private TransactionStatus status;

    private String senderWalletId;

    private String receiverWalletId;

    private Double amount;
    private String remarks;

    public TransactionModel() {
    }

    public TransactionModel(Long id, LocalDateTime transactionInitTime, LocalDateTime transactionCompletedTime, TransactionStatus status, String senderWalletId, String receiverWalletId, Double amount, String remarks) {
        this.id = id;
        this.transactionInitTime = transactionInitTime;
        this.transactionCompletedTime = transactionCompletedTime;
        this.status = status;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "id=" + id +
                ", transactionInitTime=" + transactionInitTime +
                ", transactionCompletedTime=" + transactionCompletedTime +
                ", status=" + status +
                ", senderWalletId='" + senderWalletId + '\'' +
                ", receiverWalletId='" + receiverWalletId + '\'' +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTransactionInitTime() {
        return transactionInitTime;
    }

    public void setTransactionInitTime(LocalDateTime transactionInitTime) {
        this.transactionInitTime = transactionInitTime;
    }

    public LocalDateTime getTransactionCompletedTime() {
        return transactionCompletedTime;
    }

    public void setTransactionCompletedTime(LocalDateTime transactionCompletedTime) {
        this.transactionCompletedTime = transactionCompletedTime;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSenderWalletId() {
        return senderWalletId;
    }

    public void setSenderWalletId(String senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public String getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(String receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
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
