package com.stockmanagement.hotelmanagement.controller;

import com.stockmanagement.hotelmanagement.booking.BookingRepository;
import com.stockmanagement.hotelmanagement.common.ApiResponse;
import com.stockmanagement.hotelmanagement.invoice.InvoiceRepository;
import com.stockmanagement.hotelmanagement.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/occupancy")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOccupancyReport() {
        try {
            Map<String, Object> report = new HashMap<>();
            
            Long totalRooms = roomRepository.count();
            Long occupiedRooms = (long) roomRepository.findByStatus("Occupied").size();
            Long availableRooms = totalRooms - occupiedRooms;
            Double occupancyRate = totalRooms > 0 ? 
                    (double) occupiedRooms / totalRooms * 100 : 0.0;

            report.put("totalRooms", totalRooms);
            report.put("occupiedRooms", occupiedRooms);
            report.put("availableRooms", availableRooms);
            report.put("occupancyRate", String.format("%.2f%%", occupancyRate));

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    "Occupancy report retrieved successfully",
                    report
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Failed to retrieve occupancy report", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/booking-summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBookingSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            Long totalBookings = bookingRepository.count();
            Long confirmedBookings = (long) bookingRepository.findByStatus("Confirmed").size();
            Long checkedInBookings = (long) bookingRepository.findByStatus("Checked-In").size();
            Long checkedOutBookings = (long) bookingRepository.findByStatus("Checked-Out").size();
            Long cancelledBookings = (long) bookingRepository.findByStatus("Cancelled").size();

            summary.put("totalBookings", totalBookings);
            summary.put("confirmedBookings", confirmedBookings);
            summary.put("checkedInBookings", checkedInBookings);
            summary.put("checkedOutBookings", checkedOutBookings);
            summary.put("cancelledBookings", cancelledBookings);
            summary.put("activeBookings", confirmedBookings + checkedInBookings);

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    "Booking summary retrieved successfully",
                    summary
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Failed to retrieve booking summary", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/revenue-report")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> report = new HashMap<>();
            
            BigDecimal totalRevenue = invoiceRepository.findByStatus("Paid").stream()
                    .map(invoice -> invoice.getTotalAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal pendingRevenue = invoiceRepository.findByStatus("Pending").stream()
                    .map(invoice -> invoice.getTotalAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal overdueRevenue = invoiceRepository.findOverdueInvoices().stream()
                    .map(invoice -> invoice.getTotalAmount())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            report.put("totalRevenue", totalRevenue);
            report.put("pendingRevenue", pendingRevenue);
            report.put("overdueRevenue", overdueRevenue);
            report.put("currency", "USD");

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    "Revenue report retrieved successfully",
                    report
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Failed to retrieve revenue report", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/room-utilization")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRoomUtilization() {
        try {
            Map<String, Object> utilization = new HashMap<>();
            
            // Count rooms by type
            var roomsByType = roomRepository.findAll().stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            com.stockmanagement.hotelmanagement.room.Room::getRoomType,
                            java.util.stream.Collectors.counting()
                    ));
            
            // Count occupied rooms by type
            var occupiedByType = roomRepository.findByStatus("Occupied").stream()
                    .collect(java.util.stream.Collectors.groupingBy(
                            com.stockmanagement.hotelmanagement.room.Room::getRoomType,
                            java.util.stream.Collectors.counting()
                    ));

            utilization.put("roomsByType", roomsByType);
            utilization.put("occupiedByType", occupiedByType);

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    "Room utilization report retrieved successfully",
                    utilization
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Failed to retrieve room utilization report", null, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}




