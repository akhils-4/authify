package com.akhil.authify.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileRequest {

    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email(message = "Enter a valid email address")
    @NotBlank(message = "email should not be empty")
    private String email;
    @Size(min = 6, message = "password atleast must be 6 characters")
    private String password;
}
