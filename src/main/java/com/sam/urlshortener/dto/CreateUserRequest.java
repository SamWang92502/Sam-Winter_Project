package com.sam.urlshortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CreateUserRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email addresses must have a valid format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    // Default constructor (required for Spring to map JSON)
    public CreateUserRequest() {}

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
