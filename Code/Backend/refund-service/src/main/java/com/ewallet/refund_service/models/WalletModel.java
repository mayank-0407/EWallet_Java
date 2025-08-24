package com.ewallet.refund_service.models;

import lombok.Data;

@Data
public class WalletModel {

    private String id;
    private String userId;
    private double balance;
    private Double limit;

    public WalletModel() {
    }

    public WalletModel(String userId, double balance, Double limit, String id) {
        this.userId = userId;
        this.balance = balance;
        this.limit = limit;
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WalletModel{" +
                "userId='" + userId + '\'' +
                ", balance=" + balance +
                ", limit=" + limit +
                ", id='" + id + '\'' +
                '}';
    }
}
