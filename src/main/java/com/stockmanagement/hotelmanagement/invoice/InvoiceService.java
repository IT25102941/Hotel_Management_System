package com.stockmanagement.hotelmanagement.invoice;

import com.stockmanagement.hotelmanagement.booking.Booking;
import com.stockmanagement.hotelmanagement.booking.BookingRepository;
import com.stockmanagement.hotelmanagement.common.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Invoice createInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice generateInvoiceForBooking(Long bookingId) {
        // 1. Fetch the booking by bookingId
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // 2. Generate invoice number
        String invoiceNumber = "INV-" + LocalDate.now().toString().replace("-", "") + "-" + bookingId;

        // 3. Create new invoice
        Invoice invoice = new Invoice();
        invoice.setBookingId(bookingId);
        invoice.setGuestId(booking.getGuestId());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceNumber(invoiceNumber);

        // 4. Set amount from booking total price
        BigDecimal amount = booking.getTotalPrice();
        invoice.setAmount(amount);

        // 5. Calculate tax amount (10% with proper rounding)
        BigDecimal taxAmount = amount.multiply(BigDecimal.valueOf(0.10))
            .setScale(2, RoundingMode.HALF_UP);
        invoice.setTaxAmount(taxAmount);

        // 6. Calculate total amount
        BigDecimal totalAmount = amount.add(taxAmount);
        invoice.setTotalAmount(totalAmount);

        // 7. Set payment status as unpaid
        invoice.setStatus("Unpaid");

        // 8. Set due date as 3 days after checkout
        invoice.setDueDate(booking.getCheckOutDate().plusDays(3));

        // 9. Save and return the invoice
        return invoiceRepository.save(invoice);
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice updateInvoice(Long id, Invoice invoiceDetails) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            Invoice existingInvoice = invoice.get();
            existingInvoice.setBookingId(invoiceDetails.getBookingId());
            existingInvoice.setGuestId(invoiceDetails.getGuestId());
            existingInvoice.setInvoiceDate(invoiceDetails.getInvoiceDate());
            existingInvoice.setAmount(invoiceDetails.getAmount());
            existingInvoice.setTaxAmount(invoiceDetails.getTaxAmount());
            existingInvoice.setTotalAmount(invoiceDetails.getTotalAmount());
            existingInvoice.setStatus(invoiceDetails.getStatus());
            existingInvoice.setDueDate(invoiceDetails.getDueDate());
            existingInvoice.setPaymentDate(invoiceDetails.getPaymentDate());
            existingInvoice.setPaymentMethod(invoiceDetails.getPaymentMethod());
            existingInvoice.setDescription(invoiceDetails.getDescription());
            return invoiceRepository.save(existingInvoice);
        }
        return null;
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public Optional<Invoice> getInvoiceByBookingId(Long bookingId) {
        return invoiceRepository.findByBookingId(bookingId);
    }

    public List<Invoice> getInvoicesByGuestId(Long guestId) {
        return invoiceRepository.findByGuestId(guestId);
    }

    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    public List<Invoice> getInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        return invoiceRepository.findByInvoiceDateBetween(startDate, endDate);
    }

    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findOverdueInvoices();
    }

    public void markInvoiceAsPaid(Long id, String paymentMethod) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isPresent()) {
            Invoice existingInvoice = invoice.get();
            existingInvoice.setStatus("Paid");
            existingInvoice.setPaymentDate(LocalDate.now());
            existingInvoice.setPaymentMethod(paymentMethod);
            invoiceRepository.save(existingInvoice);
        }
    }
}




