package com.stockmanagement.hotelmanagement.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalGuests;
    private Long totalRooms;
    private Long availableRooms;
    private Long occupiedRooms;
    private Long totalBookings;
    private Long activeBookings;
    private Long totalStaff;
    private BigDecimal totalRevenue;
    private BigDecimal pendingInvoicesAmount;
    private Long pendingInvoices;
    private Double occupancyRate;
}




