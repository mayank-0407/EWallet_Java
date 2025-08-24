package com.ewallet.database_service.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user_model")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    private Boolean merchant;
    private Date dob;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = true, unique = true)
    private String walletId;

    private String password;

    @OneToOne
    @JoinColumn(name = "business_details_id", referencedColumnName = "id",nullable = true)
    private BusinessDetailsModel businessDetails;

    public UserModel() {
    }

    public UserModel(Long id, String name, String email, String username, Boolean merchant, Date dob, String phoneNumber, String walletId, String password, BusinessDetailsModel businessDetails) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.merchant = merchant;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.walletId = walletId;
        this.password = password;
        this.businessDetails = businessDetails;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", merchant=" + merchant +
                ", dob=" + dob +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", walletId='" + walletId + '\'' +
                ", password='" + password + '\'' +
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BusinessDetailsModel getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetailsModel businessDetails) {
        this.businessDetails = businessDetails;
    }
}