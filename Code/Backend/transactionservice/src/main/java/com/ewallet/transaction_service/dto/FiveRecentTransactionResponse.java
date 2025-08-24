package com.ewallet.transaction_service.dto;

import com.ewallet.transaction_service.models.TransactionModel;
import lombok.Data;

@Data
public class FiveRecentTransactionResponse {
    public TransactionModel transaction;
    public String senderName;
    public String receiverName;

    public FiveRecentTransactionResponse() {
    }

    public FiveRecentTransactionResponse(TransactionModel transaction, String senderName, String receiverName) {
        this.transaction = transaction;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    @Override
    public String toString() {
        return "FiveRecentTransactionResponse{" +
                "transaction=" + transaction +
                ", senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                '}';
    }

    public TransactionModel getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
