package com.ewallet.auth_service.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String phoneNumber;
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String phoneNumber, String newPassword) {
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
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

    @Override
    public String toString() {
        return "ForgotPasswordDto{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}