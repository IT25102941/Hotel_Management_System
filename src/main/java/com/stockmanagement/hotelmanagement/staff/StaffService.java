package com.stockmanagement.hotelmanagement.staff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {
    
    @Autowired
    private StaffRepository staffRepository;

    public Staff createStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public Optional<Staff> getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    public Staff updateStaff(Long id, Staff staffDetails) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            Staff existingStaff = staff.get();
            existingStaff.setFirstName(staffDetails.getFirstName());
            existingStaff.setLastName(staffDetails.getLastName());
            existingStaff.setEmail(staffDetails.getEmail());
            existingStaff.setPhoneNumber(staffDetails.getPhoneNumber());
            existingStaff.setPosition(staffDetails.getPosition());
            existingStaff.setDepartment(staffDetails.getDepartment());
            existingStaff.setSalary(staffDetails.getSalary());
            existingStaff.setJoinDate(staffDetails.getJoinDate());
            existingStaff.setAddress(staffDetails.getAddress());
            existingStaff.setCity(staffDetails.getCity());
            existingStaff.setCountry(staffDetails.getCountry());
            existingStaff.setStatus(staffDetails.getStatus());
            existingStaff.setEmergencyContact(staffDetails.getEmergencyContact());
            existingStaff.setEmergencyContactPhone(staffDetails.getEmergencyContactPhone());
            return staffRepository.save(existingStaff);
        }
        return null;
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    public Optional<Staff> getStaffByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    public List<Staff> getStaffByPosition(String position) {
        return staffRepository.findByPosition(position);
    }

    public List<Staff> getStaffByDepartment(String department) {
        return staffRepository.findByDepartment(department);
    }

    public List<Staff> getStaffByStatus(String status) {
        return staffRepository.findByStatus(status);
    }

    public void updateStaffStatus(Long id, String status) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            Staff existingStaff = staff.get();
            existingStaff.setStatus(status);
            staffRepository.save(existingStaff);
        }
    }

    public List<Staff> searchStaffByName(String name) {
        return staffRepository.searchByName(name);
    }

    public List<Staff> getStaffBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return staffRepository.findBySalaryBetween(minSalary, maxSalary);
    }
}




