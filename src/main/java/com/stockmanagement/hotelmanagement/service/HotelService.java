package com.stockmanagement.hotelmanagement.service;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    @Column(unique = true)
    private String serviceName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Service category is required")
    private String category; // e.g., Room Service, Spa, Transport, Dining, etc.

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Status is required")
    private String status; // Available, Unavailable, Discontinued

    private String availability; // e.g., 24/7, 8am-8pm, By Request

    private String provider; // e.g., External company name or hotel department

    private Boolean requiresAdvanceBooking;

    private Integer maxCapacity;

    private String contactInfo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "Available";
        }
        if (this.requiresAdvanceBooking == null) {
            this.requiresAdvanceBooking = false;
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




