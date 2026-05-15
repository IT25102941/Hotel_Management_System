package com.stockmanagement.hotelmanagement.auth;

import com.stockmanagement.hotelmanagement.common.InvalidRequestException;
import com.stockmanagement.hotelmanagement.common.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidRequestException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidRequestException("Password is required");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (!user.getPassword().equals(password)) {
            throw new InvalidRequestException("Invalid password");
        }

        if ("Blocked".equals(user.getStatus())) {
            throw new InvalidRequestException("Your account has been blocked");
        }

        if ("Inactive".equals(user.getStatus())) {
            throw new InvalidRequestException("Your account is inactive");
        }

        // Update last login time
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    public User registerGuest(String email, String fullName, String phoneNumber, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidRequestException("Email is required");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidRequestException("Full name is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidRequestException("Password is required");
        }
        if (password.length() < 4) {
            throw new InvalidRequestException("Password must be at least 4 characters");
        }

        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("Email already exists. Please use a different email.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole("GUEST");
        user.setStatus("Active");

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}




