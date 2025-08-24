package com.ewallet.auth_service.dto;

import lombok.Data;

@Data
public class ChangePasswordwithCurrPasswordDTO {
    private String phoneNumber;
    private String newPassword;
    private String currentPassword;

    public ChangePasswordwithCurrPasswordDTO() {
    }

    public ChangePasswordwithCurrPasswordDTO(String phoneNumber, String newPassword, String currentPassword) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.currentPassword = currentPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordwithCurrPasswordDto{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", currentPassword='" + currentPassword + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}