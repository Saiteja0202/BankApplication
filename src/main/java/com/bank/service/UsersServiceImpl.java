package com.bank.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.exception.UnauthorizedAccessException;
import com.bank.model.Users;
import com.bank.model.Users.OtpStatus;
import com.bank.repository.UsersRepo;




@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepo usersRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsersServiceImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }
    
    @Autowired
    public JavaMailSender mailSender;

    @Override
    public String registerCustomer(Users users) {
        if (usersRepo.existsByName(users.getName())) {
            throw new RuntimeException("Username already taken.");
        }

        users.setPassword(hashPassword(users.getPassword()));
        users.setUserRole(Users.UserRole.CUSTOMER);
        users.setKycStatus(Users.KycStatus.PENDING);
        users.setOtpStatus(Users.OtpStatus.GENERATE);
        usersRepo.save(users);
        return "Successfully registered. Your token: " + users.getUserToken();
    }

//    @Override
//    public HashMap<String, String> loginCustomer(Users loginRequest) {
//        Users user = usersRepo.findByName(loginRequest.getName())
//                .orElseThrow(() -> new UnauthorizedAccessException("User not found."));
//
//        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new UnauthorizedAccessException("Invalid credentials.");
//        }
//
//        user.setUserToken(UUID.randomUUID().toString());
//        user.setGeneratedAt(LocalDateTime.now());
//        usersRepo.save(user);
//
//        HashMap<String, String> response = new HashMap<>();
//        response.put("userId", String.valueOf(user.getUser_id()));
//        response.put("role", user.getUserRole().name());
//        response.put("token", user.getUserToken());
//
//        return response;
//    }
    
    @Override
    public String registerAdmin(Users users) {
        if (usersRepo.existsByName(users.getName())) {
            throw new RuntimeException("Username already taken.");
        }

        users.setPassword(hashPassword(users.getPassword()));
        users.setUserRole(Users.UserRole.ADMIN);
        users.setKycStatus(Users.KycStatus.VERIFIED);
        users.setOtpStatus(Users.OtpStatus.GENERATE);

        usersRepo.save(users);
        return "Admin registered successfully. Token: " + users.getUserToken();
    }

//    @Override
//    public HashMap<String, String> loginAdmin(Users loginRequest) {
//        Users user = usersRepo.findByName(loginRequest.getName())
//                .orElseThrow(() -> new UnauthorizedAccessException("Admin not found."));
//
//        if (user.getUserRole() != Users.UserRole.ADMIN) {
//            throw new UnauthorizedAccessException("This user is not an admin.");
//        }
//
//        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
//            throw new UnauthorizedAccessException("Invalid credentials.");
//        }
//
//        user.setUserToken(UUID.randomUUID().toString());
//        user.setGeneratedAt(LocalDateTime.now());
//        usersRepo.save(user);
//
//        HashMap<String, String> response = new HashMap<>();
//        response.put("userId", String.valueOf(user.getUser_id()));
//        response.put("role", user.getUserRole().name());
//        response.put("token", user.getUserToken());
//
//        return response;
//    }
//    
    
    @Override
    public String deleteAccount(Integer userId) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        usersRepo.delete(user);
        return "Account deleted successfully.";
    }

    @Override
    public String updatePassword(Integer userId, String oldPassword, String newPassword) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect.");
        }

        user.setPassword(encoder.encode(newPassword));
        usersRepo.save(user);
        return "Password updated successfully.";
    }

    @Override
    public String updateDetails(Integer userId, Users updatedUser) {
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());

        usersRepo.save(user);
        return "User details updated successfully.";
    }


    private String hashPassword(String plainText) {
        return encoder.encode(plainText);
    }

    @Override
    public HashMap<String, String> loginCustomer(Users loginRequest) {
        Users user = usersRepo.findByName(loginRequest.getName())
                .orElseThrow(() -> new UnauthorizedAccessException("User not found."));

       
        if (user.getOtpStatus() != OtpStatus.VERIFIED) {
            throw new UnauthorizedAccessException("OTP not verified. Please verify your OTP before logging in.");
        }

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid credentials.");
        }
       

        user.setUserToken(UUID.randomUUID().toString());
        user.setGeneratedAt(LocalDateTime.now());
        user.setOTP(0);
        user.setOtpStatus(null);
        usersRepo.save(user);

        HashMap<String, String> response = new HashMap<>();
        response.put("userId", String.valueOf(user.getUser_id()));
        response.put("role", user.getUserRole().name());
        response.put("token", user.getUserToken());
        
        return response;
    }//  @Override
    public HashMap<String, String> loginAdmin(Users loginRequest) {
        Users user = usersRepo.findByName(loginRequest.getName())
                .orElseThrow(() -> new UnauthorizedAccessException("Admin not found."));

        if (user.getUserRole() != Users.UserRole.ADMIN) {
            throw new UnauthorizedAccessException("This user is not an admin.");
        }

        if (user.getOtpStatus() != OtpStatus.VERIFIED) {
            throw new UnauthorizedAccessException("OTP not verified. Please verify your OTP before logging in.");
        }


        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid credentials.");
        }

        user.setUserToken(UUID.randomUUID().toString());
        user.setGeneratedAt(LocalDateTime.now());
        user.setOTP(0);
        user.setOtpStatus(null);
        usersRepo.save(user);

        HashMap<String, String> response = new HashMap<>();
        response.put("userId", String.valueOf(user.getUser_id()));
        response.put("role", user.getUserRole().name());
        response.put("token", user.getUserToken());

        return response;
    }// 
    @Override
    public String generateOtp(String name, String email) {
        Users user = usersRepo.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + name));

        if (!user.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("Email does not match registered email for this username.");
        }

        int otp = new Random().nextInt(9000) + 1000; 

        user.setOtpStatus(Users.OtpStatus.PENDING);
        user.setOtpGeneratedAt(LocalDateTime.now());
        user.setOTP(otp);
        usersRepo.save(user);

        sendOtpEmail(email, otp);
        return "OTP has been sent to your registered email.";
    }


    private void sendOtpEmail(String to, int otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + ". It is valid for 5 minutes.");
        mailSender.send(message);
    }

    // ------------------- OTP VERIFICATION -----------------------

    @Override
    public String verifyOtp(String email, int enteredOtp) {
        Users user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (user.getOtpGeneratedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            user.setOtpStatus(Users.OtpStatus.EXPIRED);
            usersRepo.save(user);
            return "OTP expired.";
        }

        if (user.getOTP() == enteredOtp) {
            user.setOtpStatus(Users.OtpStatus.VERIFIED);
            usersRepo.save(user);
            return "OTP verified successfully.";
        } else {
            return "Invalid OTP.";
        }
    }

}
