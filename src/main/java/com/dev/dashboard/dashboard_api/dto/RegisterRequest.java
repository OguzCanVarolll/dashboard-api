package com.dev.dashboard.dashboard_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegisterRequest(
         @NotBlank(message ="Email cannot be blank")
         @Email (message = "Please provide a valid email address")
         @Size (max =100)
         String email,

         @NotBlank (message = "Password cannot be blank.")
         @Size( min = 8 , max = 20, message = "Password must be between 8 and 20 characters.")
         String password,

         @NotBlank (message ="Confirm password cannot be blank")
         @Size( min = 8 , max = 20, message = "Confirm password must be between 8 and 20 characters.")
         String confirmPassword
) {
}
