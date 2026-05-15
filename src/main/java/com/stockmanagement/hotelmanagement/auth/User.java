package com.stockmanagement.hotelmanagement.auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "role", nullable = false)
    private String role; // ADMIN, GUEST

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status", nullable = false)
    private String status; // Active, Inactive, Blocked

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "last_login")
    private java.time.LocalDateTime lastLogin;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        status = "Active";
    }
}




