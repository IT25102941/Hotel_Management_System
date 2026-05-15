package com.stockmanagement.hotelmanagement.controller;

import com.stockmanagement.hotelmanagement.booking.BookingRepository;
import com.stockmanagement.hotelmanagement.common.ApiResponse;
import com.stockmanagement.hotelmanagement.common.DashboardStats;
import com.stockmanagement.hotelmanagement.guest.GuestRepository;
import com.stockmanagement.hotelmanagement.invoice.InvoiceRepository;
import com.stockmanagement.hotelmanagement.room.RoomRepository;
import com.stockmanagement.hotelmanagement.staff.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        try {
            // Calculate statistics using optimized repository queries
            Long totalGuests = guestRepository.count();
            Long totalRooms = roomRepository.count();
            Long availableRooms = roomRepository.countByStatusAvailable();
            Long occupiedRooms = roomRepository.countByStatusOccupied();
            Long totalBookings = bookingRepository.count();
            Long activeBookings = bookingRepository.countActiveBookings();
            Long totalStaff = staffRepository.count();
            Long activeStaff = staffRepository.countByStatusActive();
            Long pendingInvoices = invoiceRepository.countByStatusUnpaid();
            
            // Calculate revenue from paid invoices
            BigDecimal totalRevenue = invoiceRepository.sumTotalAmountByStatusPaid();
            if (totalRevenue == null) {
                totalRevenue = BigDecimal.ZERO;
            }
            
            // Calculate occupancy rate
            Double occupancyRate = totalRooms > 0 ? 
                    (double) occupiedRooms / totalRooms * 100 : 0.0;

            DashboardStats stats = new DashboardStats(
                    totalGuests,
                    totalRooms,
                    availableRooms,
                    occupiedRooms,
                    totalBookings,
                    activeBookings,
                    activeStaff,
                    totalRevenue,
                    totalRevenue, // pending invoices amount (using revenue for backward compatibility)
                    pendingInvoices,
                    occupancyRate
            );

            ApiResponse<DashboardStats> response = new ApiResponse<>(
                    true,
                    "Dashboard statistics retrieved successfully",
                    stats
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<DashboardStats> response = new ApiResponse<>(
                    false,
                    "Failed to retrieve dashboard statistics",
                    null,
                    e.getMessage()
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




