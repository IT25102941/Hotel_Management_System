package com.stockmanagement.hotelmanagement.invoice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @NotNull(message = "Booking ID is required")
    @Column(unique = true)
    private Long bookingId;

    @NotNull(message = "Guest ID is required")
    private Long guestId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private BigDecimal taxAmount;

    private BigDecimal totalAmount;

    @NotNull(message = "Status is required")
    private String status; // Pending, Paid, Overdue, Cancelled

    private LocalDate dueDate;

    private LocalDate paymentDate;

    private String paymentMethod; // Cash, Credit Card, Bank Transfer, Check

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "Pending";
        }
        if (this.invoiceDate == null) {
            this.invoiceDate = LocalDate.now();
        }
        if (this.taxAmount == null) {
            this.taxAmount = BigDecimal.ZERO;
        }
        if (this.totalAmount == null) {
            this.totalAmount = this.amount.add(this.taxAmount);
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}




