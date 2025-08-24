package com.ewallet.auth_service.dto;

import lombok.Data;

@Data
public class UserWalletIdUpdateDTO {
    private Long userId;
    private String walletId;

    public UserWalletIdUpdateDTO() {
    }

    public UserWalletIdUpdateDTO(Long userId, String walletId) {
        this.userId = userId;
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }
}