package com.ewallet.wallet_service.models;

public class PinVerificationModel {

    private String walletId;
    private String pinHash;

    public PinVerificationModel() {
    }

    public PinVerificationModel(String walletId, String pinHash) {
        this.walletId = walletId;
        this.pinHash = pinHash;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }
}