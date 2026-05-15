package com.stockmanagement.hotelmanagement.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestId(Long guestId);
    List<Booking> findByRoomId(Long roomId);
    List<Booking> findByStatus(String status);
    
    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId AND b.status IN ('Confirmed', 'Checked-In') AND " +
           "((b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId, 
                                          @Param("checkInDate") LocalDate checkInDate,
                                          @Param("checkOutDate") LocalDate checkOutDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status IN ('Confirmed', 'Checked-In')")
    Long countActiveBookings();

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
           "WHERE b.roomId = :roomId " +
           "AND b.id != :excludeBookingId " +
           "AND b.status IN ('Pending', 'Confirmed', 'Checked-In') " +
           "AND b.checkInDate < :checkOut " +
           "AND b.checkOutDate > :checkIn")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                      @Param("checkIn") LocalDate checkIn,
                                      @Param("checkOut") LocalDate checkOut,
                                      @Param("excludeBookingId") Long excludeBookingId);
}



