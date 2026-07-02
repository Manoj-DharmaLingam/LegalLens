package com.LegalLens.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "You have no eyes?")
    private String username;

    @NotBlank(message = "Well u r leaving me empty right? that means u r a hacker ! oh god get out man or i will call cops on you")
    private String password;

    public LoginRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
