package com.stockmanagement.hotelmanagement.booking;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            return new ResponseEntity<>(booking.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody Booking bookingDetails) {
        try {
            Booking updatedBooking = bookingService.updateBooking(id, bookingDetails);
            if (updatedBooking != null) {
                return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
            }
            return new ResponseEntity<>("Booking not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return new ResponseEntity<>("Booking deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Booking>> getBookingsByGuestId(@PathVariable Long guestId) {
        List<Booking> bookings = bookingService.getBookingsByGuestId(guestId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Booking>> getBookingsByRoomId(@PathVariable Long roomId) {
        List<Booking> bookings = bookingService.getBookingsByRoomId(roomId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            bookingService.updateBookingStatus(id, status);
            return new ResponseEntity<>("Booking status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}




