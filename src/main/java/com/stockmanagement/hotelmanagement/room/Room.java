package com.stockmanagement.hotelmanagement.room;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", unique = true)
    private String roomNumber;

    @NotBlank(message = "Room type is required")
    @Column(name = "room_type")
    private String roomType; // e.g., Single, Double, Suite, Deluxe

    @NotNull(message = "Price per night is required")
    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    private String description;

    private String amenities; // Comma-separated list: WiFi, AC, TV, etc.

    @NotBlank(message = "Status is required")
    private String status; // Available, Occupied, Maintenance, Reserved


    @Column(name = "bed_type")
    private String bedType; // Single, Double, Queen, King

    private Integer floor;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Transient
    public boolean isAvailable() {
        return "Available".equals(this.status);
    }

    @Transient
    public String getDisplayName() {
        return String.format("Room %s - %s (%s Bed)", this.roomNumber, this.roomType, this.bedType);
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




