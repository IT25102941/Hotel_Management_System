package com.stockmanagement.hotelmanagement.invoice;

import com.stockmanagement.hotelmanagement.booking.Booking;
import com.stockmanagement.hotelmanagement.booking.BookingRepository;
import com.stockmanagement.hotelmanagement.guest.Guest;
import com.stockmanagement.hotelmanagement.guest.GuestRepository;
import com.stockmanagement.hotelmanagement.room.Room;
import com.stockmanagement.hotelmanagement.room.RoomRepository;
import com.stockmanagement.hotelmanagement.common.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<?> createInvoice(@Valid @RequestBody Invoice invoice) {
        try {
            Invoice createdInvoice = invoiceService.createInvoice(invoice);
            return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @Valid @RequestBody Invoice invoiceDetails) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDetails);
            if (updatedInvoice != null) {
                return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
            }
            return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return new ResponseEntity<>("Invoice deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<?> getInvoiceByBookingId(@PathVariable Long bookingId) {
        Optional<Invoice> invoice = invoiceService.getInvoiceByBookingId(bookingId);
        if (invoice.isPresent()) {
            return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/booking/{bookingId}/generate")
    public ResponseEntity<?> generateInvoiceForBooking(@PathVariable Long bookingId) {
        try {
            Optional<Invoice> existing = invoiceService.getInvoiceByBookingId(bookingId);
            if (existing.isPresent()) {
                return new ResponseEntity<>(existing.get(), HttpStatus.OK);
            }
            Invoice created = invoiceService.generateInvoiceForBooking(bookingId);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/guest/{guestId}")
    public ResponseEntity<List<Invoice>> getInvoicesByGuestId(@PathVariable Long guestId) {
        List<Invoice> invoices = invoiceService.getInvoicesByGuestId(guestId);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@PathVariable String status) {
        List<Invoice> invoices = invoiceService.getInvoicesByStatus(status);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Invoice>> getInvoicesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        List<Invoice> invoices = invoiceService.getInvoicesByDateRange(
            LocalDate.parse(startDate),
            LocalDate.parse(endDate)
        );
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/overdue/list")
    public ResponseEntity<List<Invoice>> getOverdueInvoices() {
        List<Invoice> invoices = invoiceService.getOverdueInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<?> markInvoiceAsPaid(@PathVariable Long id, @RequestParam String paymentMethod) {
        try {
            invoiceService.markInvoiceAsPaid(id, paymentMethod);
            return new ResponseEntity<>("Invoice marked as paid", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Print Invoice - Web endpoint that renders the invoice print view
     */
    @GetMapping("/{id}/print")
    public org.springframework.web.servlet.ModelAndView printInvoice(@PathVariable Long id) {
        // 1. Fetch invoice
        Optional<Invoice> invoiceOpt = invoiceService.getInvoiceById(id);
        if (invoiceOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        Invoice invoice = invoiceOpt.get();
        
        // 2. Fetch booking
        Optional<Booking> bookingOpt = bookingRepository.findById(invoice.getBookingId());
        if (bookingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Booking not found");
        }
        Booking booking = bookingOpt.get();
        
        // 3. Fetch guest
        Optional<Guest> guestOpt = guestRepository.findById(booking.getGuestId());
        if (guestOpt.isEmpty()) {
            throw new ResourceNotFoundException("Guest not found");
        }
        Guest guest = guestOpt.get();
        
        // 4. Fetch room
        Optional<Room> roomOpt = roomRepository.findById(booking.getRoomId());
        if (roomOpt.isEmpty()) {
            throw new ResourceNotFoundException("Room not found");
        }
        Room room = roomOpt.get();
        
        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        
        // Add to model
        org.springframework.web.servlet.ModelAndView mav = new org.springframework.web.servlet.ModelAndView("invoice-print");
        mav.addObject("invoice", invoice);
        mav.addObject("booking", booking);
        mav.addObject("guest", guest);
        mav.addObject("room", room);
        mav.addObject("nights", nights);
        
        return mav;
    }
}




