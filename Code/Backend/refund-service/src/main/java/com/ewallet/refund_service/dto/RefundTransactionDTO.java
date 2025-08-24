package com.ewallet.refund_service.dto;

import lombok.Data;
import com.ewallet.refund_service.models.WalletModel;

@Data
public class RefundTransactionDTO {

    private WalletModel senderWallet;
    private Long transactionId;
    private Double refundAmount;

    public RefundTransactionDTO() {
    }

    public RefundTransactionDTO(WalletModel senderWallet, Long transactionId, Double refundAmount) {
        this.senderWallet = senderWallet;
        this.transactionId = transactionId;
        this.refundAmount = refundAmount;
    }

    @Override
    public String toString() {
        return "RefundTransactionDTO{" +
                "senderWallet=" + senderWallet +
                ", transactionId=" + transactionId +
                ", refundAmount=" + refundAmount +
                '}';
    }

    public WalletModel getSenderWallet() {
        return senderWallet;
    }

    public void setSenderWallet(WalletModel senderWallet) {
        this.senderWallet = senderWallet;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }
}
