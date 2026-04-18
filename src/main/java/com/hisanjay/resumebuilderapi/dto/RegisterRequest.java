package com.hisanjay.resumebuilderapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 20, message = "Name should be between 2 and 20 characters")
    private String name;
    @NotBlank(message = "Email is required")

    @Email(message = "Email Should be Valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 32, message = "Password should be between 6 and 32 characters")
    private String password;
    private String profileImageUrl;
}
