package com.stockmanagement.hotelmanagement.staff;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Position is required")
    private String position; // e.g., Manager, Receptionist, Housekeeper, Chef, etc.

    private String department; // e.g., Front Desk, Housekeeping, Kitchen, etc.

    @NotNull(message = "Salary is required")
    private BigDecimal salary;

    @NotNull(message = "Join date is required")
    private LocalDate joinDate;

    private String address;

    private String city;

    private String country;

    @NotBlank(message = "Status is required")
    private String status; // Active, Inactive, On Leave, Terminated

    private String emergencyContact;

    private String emergencyContactPhone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "Active";
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




