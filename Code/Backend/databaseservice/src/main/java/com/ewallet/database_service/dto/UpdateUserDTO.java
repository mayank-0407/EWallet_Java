package com.ewallet.database_service.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String name;
    private String email;
    private String username;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
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
}