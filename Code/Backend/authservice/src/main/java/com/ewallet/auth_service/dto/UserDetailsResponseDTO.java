package com.ewallet.auth_service.dto;

import com.ewallet.auth_service.models.BusinessDetailsModel;
import com.ewallet.auth_service.models.UserModel;
import lombok.Data;

import java.util.Date;

@Data
public class UserDetailsResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String username;

    private Boolean merchant;
    private Date dob;
    private String phoneNumber;
    private String walletId;

    private BusinessDetailsModel businessDetails;

    public UserDetailsResponseDTO() {
    }

    public UserDetailsResponseDTO(UserModel thisUser){
        this.id = thisUser.getId();
        this.name = thisUser.getName();
        this.email = thisUser.getEmail();
        this.username = thisUser.getUsername();
        this.merchant = thisUser.getMerchant();
        this.dob = thisUser.getDob();
        this.phoneNumber = thisUser.getPhoneNumber();
        this.walletId = thisUser.getWalletId();
        this.businessDetails = thisUser.getBusinessDetails();
    }

    public UserDetailsResponseDTO(Long id, String name, String email, String username, Boolean merchant, Date dob, String phoneNumber, String walletId, BusinessDetailsModel businessDetails) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.merchant = merchant;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.walletId = walletId;
        this.businessDetails = businessDetails;
    }

    @Override
    public String toString() {
        return "UserDetailsResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", merchant=" + merchant +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", walletId='" + walletId + '\'' +
                ", businessDetails=" + businessDetails +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getMerchant() {
        return merchant;
    }

    public void setMerchant(Boolean merchant) {
        this.merchant = merchant;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public BusinessDetailsModel getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetailsModel businessDetails) {
        this.businessDetails = businessDetails;
    }
}
