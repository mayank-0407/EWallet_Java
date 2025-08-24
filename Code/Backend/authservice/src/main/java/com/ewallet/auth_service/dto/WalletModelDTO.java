package com.ewallet.auth_service.dto;

import lombok.Data;

@Data
public class WalletModelDTO {

    private String id;
    private String userId;
    private double balance;
    private Double limit;

    public WalletModelDTO() {
    }

    public WalletModelDTO(String userId, double balance, Double limit, String id) {
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

    public String getid() {
        return id;
    }

    public void setid(String id) {
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
