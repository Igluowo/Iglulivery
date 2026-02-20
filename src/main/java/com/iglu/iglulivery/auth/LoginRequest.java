package com.iglu.iglulivery.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "The email is required")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "The password is required")
    private String password;
}
