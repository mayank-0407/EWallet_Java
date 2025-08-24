package com.ewallet.database_service.dto;

import lombok.Data;

@Data
public class WalletInitDTO {
    private String userId;
    private Double limit;

    public WalletInitDTO() {
    }

    public WalletInitDTO(String userId, Double limit) {
        this.userId = userId;
        this.limit = limit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }
}
