package com.bank.service;

import java.time.LocalDateTime;
import java.util.HashMap;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.exception.UnauthorizedAccessException;
import com.bank.model.Users;
import com.bank.repository.UsersRepo;




@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepo usersRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsersServiceImpl(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    @Override
    public String registerCustomer(Users users) {
        if (usersRepo.existsByName(users.getName())) {
            throw new RuntimeException("Username already taken.");
        }

        users.setPassword(hashPassword(users.getPassword()));
        users.setUserRole(Users.UserRole.CUSTOMER);
        users.setKycStatus(Users.KycStatus.PENDING);
        usersRepo.save(users);
        return "Successfully registered. Your token: " + users.getUserToken();
    }

    @Override
    public HashMap<String, String> loginCustomer(Users loginRequest) {
        Users user = usersRepo.findByName(loginRequest.getName())
                .orElseThrow(() -> new UnauthorizedAccessException("User not found."));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid credentials.");
        }

        user.setUserToken(UUID.randomUUID().toString());
        user.setGeneratedAt(LocalDateTime.now());
        usersRepo.save(user);

        HashMap<String, String> response = new HashMap<>();
        response.put("userId", String.valueOf(user.getUser_id()));
        response.put("role", user.getUserRole().name());
        response.put("token", user.getUserToken());

        return response;
    }
    
    @Override
    public String registerAdmin(Users users) {
        if (usersRepo.existsByName(users.getName())) {
            throw new RuntimeException("Username already taken.");
        }

        users.setPassword(hashPassword(users.getPassword()));
        users.setUserRole(Users.UserRole.ADMIN);
        users.setKycStatus(Users.KycStatus.VERIFIED);

        usersRepo.save(users);
        return "Admin registered successfully. Token: " + users.getUserToken();
    }

    @Override
    public HashMap<String, String> loginAdmin(Users loginRequest) {
        Users user = usersRepo.findByName(loginRequest.getName())
                .orElseThrow(() -> new UnauthorizedAccessException("Admin not found."));

        if (user.getUserRole() != Users.UserRole.ADMIN) {
            throw new UnauthorizedAccessException("This user is not an admin.");
        }

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedAccessException("Invalid credentials.");
        }

        user.setUserToken(UUID.randomUUID().toString());
        user.setGeneratedAt(LocalDateTime.now());
        usersRepo.save(user);

        HashMap<String, String> response = new HashMap<>();
        response.put("userId", String.valueOf(user.getUser_id()));
        response.put("role", user.getUserRole().name());
        response.put("token", user.getUserToken());

        return response;
    }
    
    
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
}
