package com.ewallet.wallet_service.dto;

public class ChangeWalletPinDTO {
    private String walletId;
    private String oldPin;
    private String newPin;

    public ChangeWalletPinDTO() {
    }

    public ChangeWalletPinDTO(String walletId, String oldPin, String newPin) {
        this.walletId = walletId;
        this.oldPin = oldPin;
        this.newPin = newPin;
    }

    @Override
    public String toString() {
        return "ChangeWalletPinDTO{" +
                "walletId='" + walletId + '\'' +
                ", oldPin='" + oldPin + '\'' +
                ", newPin='" + newPin + '\'' +
                '}';
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}
