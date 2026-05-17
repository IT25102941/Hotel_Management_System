package com.stockmanagement.hotelmanagement.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findByStatus(String status);
    List<Room> findByRoomType(String roomType);
    List<Room> findByFloor(Integer floor);
    
    @Query("SELECT r FROM Room r WHERE r.status = 'Available'")
    List<Room> findAvailableRooms();

    @Query("SELECT DISTINCT r FROM Room r " +
           "WHERE r.status = 'Available' " +
           "AND NOT EXISTS (" +
           "  SELECT 1 FROM Booking b " +
           "  WHERE b.roomId = r.id " +
           "  AND b.status IN ('Pending', 'Confirmed', 'Checked-In') " +
           "  AND b.checkInDate < :checkOut " +
           "  AND b.checkOutDate > :checkIn " +
           ")")
    List<Room> findAvailableRooms(@Param("checkIn") LocalDate checkIn, 
                                  @Param("checkOut") LocalDate checkOut);
    
    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = 'Available'")
    Long countByStatusAvailable();
    
    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = 'Occupied'")
    Long countByStatusOccupied();
}




