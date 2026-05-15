package com.stockmanagement.hotelmanagement.controller;

import com.stockmanagement.hotelmanagement.guest.Guest;
import com.stockmanagement.hotelmanagement.guest.GuestRepository;
import com.stockmanagement.hotelmanagement.invoice.Invoice;
import com.stockmanagement.hotelmanagement.invoice.InvoiceService;
import com.stockmanagement.hotelmanagement.booking.Booking;
import com.stockmanagement.hotelmanagement.booking.BookingRepository;
import com.stockmanagement.hotelmanagement.room.Room;
import com.stockmanagement.hotelmanagement.room.RoomRepository;
import com.stockmanagement.hotelmanagement.common.ResourceNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    
    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * Home page - Landing page for the Hotel Management System
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * Dashboard - Main dashboard with module navigation
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    /**
     * Old index endpoint - Redirect to login
     */
    @GetMapping("/")
    public String index() {
        return "login";
    }

    /**
     * CRUD Pages
     */
    @GetMapping("/guests")
    public String guests(@RequestParam(required = false) String q, Model model) {
        List<Guest> guests;
        
        // If search query is provided and not empty, search; otherwise get all guests
        if (q != null && !q.trim().isEmpty()) {
            guests = guestRepository.searchGuests(q.trim());
            model.addAttribute("searchQuery", q.trim());
        } else {
            guests = guestRepository.findAll();
            model.addAttribute("searchQuery", "");
        }
        
        model.addAttribute("guests", guests);
        return "guests";
    }

    @GetMapping("/rooms")
    public String rooms() {
        return "rooms";
    }

    @GetMapping("/bookings")
    public String bookings(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            model.addAttribute("userEmail", userEmail);
        }
        return "bookings";
    }

    @GetMapping("/invoices")
    public String invoices() {
        return "invoices";
    }

    @GetMapping("/staff")
    public String staff() {
        return "staff";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    /**
     * Guest Dashboard - Dashboard for logged-in guests
     */
    @GetMapping("/guest-dashboard")
    public String guestDashboard() {
        return "guest-dashboard";
    }

    /**
     * Book a Room - Page for guests to search and book available rooms
     */
    @GetMapping("/book-room")
    public String bookRoom() {
        return "book-room";
    }

    /**
     * Access Denied - Shown when guests try to access admin-only pages
     */
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    /**
     * Guest Invoices - Page for guests to view their invoices
     */
    @GetMapping("/guest-invoices")
    public String guestInvoices(Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail != null) {
            model.addAttribute("userEmail", userEmail);
        }
        return "guest-invoices";
    }

    /**
     * Guest Invoice Detail - Page for guests to view a specific invoice
     */
    @GetMapping("/guest-invoices/{id}")
    public String guestInvoiceDetail(@PathVariable Long id, Model model) {
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
        model.addAttribute("invoice", invoice);
        model.addAttribute("booking", booking);
        model.addAttribute("guest", guest);
        model.addAttribute("room", room);
        model.addAttribute("nights", nights);

        return "guest-invoice-detail";
    }
}
