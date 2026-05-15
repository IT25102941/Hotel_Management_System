package com.stockmanagement.hotelmanagement.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelServiceRepository extends JpaRepository<HotelService, Long> {
    Optional<HotelService> findByServiceName(String serviceName);
    List<HotelService> findByCategory(String category);
    List<HotelService> findByStatus(String status);
    List<HotelService> findByProvider(String provider);
}




