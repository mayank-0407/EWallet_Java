package com.ewallet.payment_service.models;

import lombok.Data;

@Data
public class BusinessDetailsModel {
    private Long id;
    private String businessName;
    private String industry;
    private String address;
    private String city;
    private String state;
    private String zipCode;

    public BusinessDetailsModel() {
    }

    public BusinessDetailsModel(Long id, String businessName, String industry, String address, String city, String state, String zipCode) {
        this.id = id;
        this.businessName = businessName;
        this.industry = industry;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
