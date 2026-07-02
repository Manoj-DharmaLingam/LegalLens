package com.LegalLens.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank(message="Dont u see this ?!!")
    private String username;

    private String password;

    @Email(message="Yoo Bro This is an Email not a words!!!")
    private String email;

    @NotBlank(message="well well well You missed this")
    private String fullName;

    @NotBlank(message="Tell me who are you are get out!!!")
    private String role;

    public RegisterRequest() {}

    @NotBlank
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }
    public String getPassword() { 
        return password; 
    }
    public void setPassword(String password) { 
        this.password = password; 
    }

    @Email
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) {
         this.email = email; 
        }

    public String getFullName() { 
        return fullName; 
    }
    public void setFullName(String fullName) { 
        this.fullName = fullName; 
    }

    public String getRole() { 
        return role; 
    }
    public void setRole(String role) { 
        this.role = role; 
    }
}
