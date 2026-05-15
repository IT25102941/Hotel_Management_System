package com.stockmanagement.hotelmanagement.controller;

import com.stockmanagement.hotelmanagement.auth.User;
import com.stockmanagement.hotelmanagement.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/check-admin")
    public ResponseEntity<?> checkAdmin() {
        Optional<User> admin = userRepository.findByEmail("admin@hotel.com");

        if (admin.isPresent()) {
            User user = admin.get();
            return ResponseEntity.ok(new DebugResponse(
                "ADMIN_FOUND",
                "Admin user exists in database",
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getStatus()
            ));
        } else {
            return ResponseEntity.ok(new DebugResponse(
                "ADMIN_NOT_FOUND",
                "Admin user does NOT exist in database. Run the SQL below to create it.",
                null,
                "admin@hotel.com",
                "Administrator",
                "ADMIN",
                "Active"
            ));
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin() {
        // Check if admin already exists
        Optional<User> existingAdmin = userRepository.findByEmail("admin@hotel.com");
        if (existingAdmin.isPresent()) {
            return ResponseEntity.ok(new DebugResponse(
                "ADMIN_EXISTS",
                "Admin user already exists in database",
                existingAdmin.get().getId(),
                existingAdmin.get().getEmail(),
                existingAdmin.get().getFullName(),
                existingAdmin.get().getRole(),
                existingAdmin.get().getStatus()
            ));
        }

        // Create new admin user
        User admin = new User();
        admin.setEmail("admin@hotel.com");
        admin.setPassword("admin123");
        admin.setFullName("Administrator");
        admin.setRole("ADMIN");
        admin.setPhoneNumber("+1-555-0000");
        admin.setStatus("Active");

        User savedAdmin = userRepository.save(admin);

        return ResponseEntity.ok(new DebugResponse(
            "ADMIN_CREATED",
            "Admin user has been created successfully",
            savedAdmin.getId(),
            savedAdmin.getEmail(),
            savedAdmin.getFullName(),
            savedAdmin.getRole(),
            savedAdmin.getStatus()
        ));
    }

    // Inner class for debug response
    public static class DebugResponse {
        public String status;
        public String message;
        public Long userId;
        public String email;
        public String fullName;
        public String role;
        public String accountStatus;

        public DebugResponse(String status, String message, Long userId, String email,
                           String fullName, String role, String accountStatus) {
            this.status = status;
            this.message = message;
            this.userId = userId;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
            this.accountStatus = accountStatus;
        }
    }
}

