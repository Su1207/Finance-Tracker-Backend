package com.demo.personalfinancetracker.dto;

import com.demo.personalfinancetracker.model.User;

public class UserDTO {
    private final Long id;
    private final String username;
    private final String fullName;
    private final String phoneNumber;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
    }

    // Getters only — no setters to keep it immutable in response

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
