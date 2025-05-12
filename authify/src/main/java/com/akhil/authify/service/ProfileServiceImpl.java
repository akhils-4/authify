package com.akhil.authify.service;

import com.akhil.authify.enity.UserEntity;
import com.akhil.authify.io.ProfileRequest;
import com.akhil.authify.io.ProfileResponse;
import com.akhil.authify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements  ProfileService{

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
       UserEntity newProfile= convertToUserEntity(request);
       if(!userRepository.existsByEmail(request.getEmail())){
           newProfile = userRepository.save(newProfile);
           return convertToProfileResponse(newProfile);

       }
       throw  new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists ");

    }

    @Override
    public ProfileResponse getProfile(String email) {
       UserEntity existingUser =  userRepository.findByEmail(email)
               .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
       return convertToProfileResponse(existingUser);

    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found "+email));

        //Generate 6 digits otp
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //calculate expiry time (currentTIme + 15 min in milliseconds)
        long expiryTime = System.currentTimeMillis()+(15 * 60 *1000);



        //update profile/user
        existingUser.setResetOtp(otp);
        existingUser.setResetOptExpireAt(expiryTime);

        //save into the database
        userRepository.save(existingUser);

        try{
            emailService.sendResetOtpEmail(existingUser.getEmail(),existingUser.getResetOtp());
        }catch (Exception ex){
            throw new RuntimeException("Unable to send email");

        }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));

        if(existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(existingUser.getResetOptExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");

        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOptExpireAt(0L);

        userRepository.save(existingUser);

        try {
            emailService.sendPasswordResetSuccess(existingUser.getEmail(), existingUser.getName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset success email");
        }

    }

    @Override
    public void sendOtp(String email) {
       UserEntity existingUser =  userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));

       if(existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()){
           return;
       }

       //Generate 6 digit OTP
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //calculate expiry time (currentTIme + 24 hours in milliseconds)
        long expiryTime = System.currentTimeMillis()+(24 * 60 * 60 *1000);

        //Update UserEntity

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);

        // save to database
        userRepository.save(existingUser);

        try{
            emailService.sendOtpEmail(existingUser.getEmail(),otp);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send email");
        }

    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity existingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Username not found "+email));

        if(existingUser.getVerifyOtp() == null || !existingUser.getVerifyOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }

        if(existingUser.getVerifyOtpExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }

        existingUser.setIsAccountVerified(true);
        existingUser.setVerifyOtp(null);
        existingUser.setVerifyOtpExpireAt(0L);

        userRepository.save(existingUser);

        try {
            emailService.sendEmailVerifiedSuccess(existingUser.getEmail(), existingUser.getName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification success email");
        }
    }

   
    private ProfileResponse convertToProfileResponse(UserEntity newProfile) {
       return  ProfileResponse.builder()
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .userid(newProfile.getUserId())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOptExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .resetOtp(null)
                .build();

    }
}
