package com.stockmanagement.hotelmanagement.booking;

import com.stockmanagement.hotelmanagement.invoice.InvoiceService;
import com.stockmanagement.hotelmanagement.room.Room;
import com.stockmanagement.hotelmanagement.room.RoomRepository;
import com.stockmanagement.hotelmanagement.common.InvalidRequestException;
import com.stockmanagement.hotelmanagement.common.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private RoomRepository roomRepository;

    public Booking createBooking(Booking booking) {
        // Validate and enrich booking with calculated data
        validateAndEnrichBooking(booking);
        
        // Check for conflicting bookings
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
            booking.getRoomId(),
            booking.getCheckInDate(),
            booking.getCheckOutDate()
        );
        
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Room is not available for the selected dates");
        }
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Auto-generate invoice
        invoiceService.generateInvoiceForBooking(savedBooking.getId());

        return savedBooking;
    }


    private void validateAndEnrichBooking(Booking booking) {
        // 1. Validate check-out date is after check-in date
        if (booking.getCheckOutDate().isBefore(booking.getCheckInDate()) || 
            booking.getCheckOutDate().isEqual(booking.getCheckInDate())) {
            throw new InvalidRequestException("Check-out date must be after check-in date");
        }

        // 2. Fetch and validate room exists
        Room room = roomRepository.findById(booking.getRoomId())
            .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        // 3. Validate room is available
        if (!room.isAvailable()) {
            throw new InvalidRequestException("Room is not available for booking");
        }

        // 4. Validate number of guests doesn't exceed room capacity
        if (booking.getNumberOfGuests() > room.getCapacity()) {
            throw new InvalidRequestException("Number of guests exceeds room capacity");
        }

        // 5. Check for overlapping bookings (exclude current booking if updating)
        Long excludeBookingId = booking.getId() != null ? booking.getId() : -1L;
        if (bookingRepository.existsOverlappingBooking(
                booking.getRoomId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                excludeBookingId)) {
            throw new InvalidRequestException("Room " + booking.getRoomId() + " is already booked for those dates");
        }

        // 6. Auto-calculate total price
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalPrice(room.getPricePerNight().multiply(BigDecimal.valueOf(nights)));
    }

    // ...existing code...


    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking existingBooking = booking.get();
            existingBooking.setGuestId(bookingDetails.getGuestId());
            existingBooking.setRoomId(bookingDetails.getRoomId());
            existingBooking.setCheckInDate(bookingDetails.getCheckInDate());
            existingBooking.setCheckOutDate(bookingDetails.getCheckOutDate());
            existingBooking.setNumberOfGuests(bookingDetails.getNumberOfGuests());
            existingBooking.setStatus(bookingDetails.getStatus());
            existingBooking.setSpecialRequests(bookingDetails.getSpecialRequests());
            
            // Validate and enrich booking with calculated data
            validateAndEnrichBooking(existingBooking);
            
            return bookingRepository.save(existingBooking);
        }
        return null;
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getBookingsByGuestId(Long guestId) {
        return bookingRepository.findByGuestId(guestId);
    }

    public List<Booking> getBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    public void updateBookingStatus(Long id, String status) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) {
            Booking existingBooking = booking.get();
            existingBooking.setStatus(status);
            bookingRepository.save(existingBooking);
        }
    }
}




