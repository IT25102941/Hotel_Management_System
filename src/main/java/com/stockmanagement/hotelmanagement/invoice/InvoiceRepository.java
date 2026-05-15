package com.stockmanagement.hotelmanagement.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByBookingId(Long bookingId);
    List<Invoice> findByGuestId(Long guestId);
    List<Invoice> findByStatus(String status);
    List<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT i FROM Invoice i WHERE i.status = 'Pending' AND i.dueDate < CURRENT_DATE")
    List<Invoice> findOverdueInvoices();
    
    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i WHERE i.status = 'Paid'")
    BigDecimal sumTotalAmountByStatusPaid();
    
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = 'Unpaid'")
    Long countByStatusUnpaid();
}




