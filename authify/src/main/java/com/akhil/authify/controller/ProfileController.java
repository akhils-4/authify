package com.akhil.authify.controller;

import com.akhil.authify.io.ProfileRequest;
import com.akhil.authify.io.ProfileResponse;
import com.akhil.authify.service.EmailService;
import com.akhil.authify.service.ProfileService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileService profileService;
    private final EmailService emailService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid  @RequestBody ProfileRequest request) throws MessagingException {

        ProfileResponse response = profileService.createProfile(request);

       emailService.sendWelcomeEmail(response.getEmail(), response.getName());

        return response;

    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email){
       return profileService.getProfile(email);
    }
}
