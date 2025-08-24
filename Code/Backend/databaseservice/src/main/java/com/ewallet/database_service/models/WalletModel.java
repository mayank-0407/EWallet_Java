package com.ewallet.database_service.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WalletModel {

    @Id
    @Column(name = "wallet_id", nullable = false,unique = true)
    private String id;

    @Column(name = "user_id", nullable = false,unique = true)
    private String userId;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "wallet_limit")
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

    public String getid() {
        return id;
    }

    public String setid(String id) {
        this.id = id;
        return id;
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
