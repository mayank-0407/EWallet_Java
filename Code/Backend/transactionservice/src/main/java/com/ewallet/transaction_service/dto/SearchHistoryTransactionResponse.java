package com.ewallet.transaction_service.dto;

import com.ewallet.transaction_service.models.TransactionModel;
import lombok.Data;

@Data
public class SearchHistoryTransactionResponse {
    public TransactionModel transaction;
    public String name;

    public SearchHistoryTransactionResponse() {
    }

    public SearchHistoryTransactionResponse(TransactionModel transaction, String name) {
        this.transaction = transaction;
        this.name = name;
    }

    @Override
    public String toString() {
        return "RecentTransactionResponse{" +
                "transaction=" + transaction +
                ", name='" + name + '\'' +
                '}';
    }

    public TransactionModel getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionModel transaction) {
        this.transaction = transaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
