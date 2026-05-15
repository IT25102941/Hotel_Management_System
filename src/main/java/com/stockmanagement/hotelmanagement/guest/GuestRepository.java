package com.stockmanagement.hotelmanagement.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByEmail(String email);
    Optional<Guest> findByIdNumber(String idNumber);
    Optional<Guest> findByPhoneNumber(String phoneNumber);
    
    @Query("SELECT g FROM Guest g WHERE LOWER(CONCAT(g.firstName, ' ', g.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Guest> searchByName(String name);
    
    @Query("SELECT g FROM Guest g WHERE " +
           "LOWER(g.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(g.phoneNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Guest> searchGuests(String query);
    
    List<Guest> findByCity(String city);
    List<Guest> findByCountry(String country);
}




