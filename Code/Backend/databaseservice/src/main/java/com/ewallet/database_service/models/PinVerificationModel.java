package com.ewallet.database_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class PinVerificationModel {

    @Id
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