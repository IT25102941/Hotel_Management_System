package com.stockmanagement.hotelmanagement.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    List<Staff> findByPosition(String position);
    List<Staff> findByDepartment(String department);
    List<Staff> findByStatus(String status);
    
    @Query("SELECT s FROM Staff s WHERE LOWER(CONCAT(s.firstName, ' ', s.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Staff> searchByName(String name);
    
    List<Staff> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);
    
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.status = 'Active'")
    Long countByStatusActive();
}




