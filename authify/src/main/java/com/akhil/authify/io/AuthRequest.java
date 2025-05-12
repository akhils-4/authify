package com.akhil.authify.io;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.PrimitiveIterator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
    private  String email;
    private  String password;
}
