package com.stockmanagement.hotelmanagement.staff;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping
    public ResponseEntity<?> createStaff(@Valid @RequestBody Staff staff) {
        try {
            Staff createdStaff = staffService.createStaff(staff);
            return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffById(@PathVariable Long id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        if (staff.isPresent()) {
            return new ResponseEntity<>(staff.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @Valid @RequestBody Staff staffDetails) {
        try {
            Staff updatedStaff = staffService.updateStaff(id, staffDetails);
            if (updatedStaff != null) {
                return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
            }
            return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/email")
    public ResponseEntity<?> getStaffByEmail(@RequestParam String email) {
        Optional<Staff> staff = staffService.getStaffByEmail(email);
        if (staff.isPresent()) {
            return new ResponseEntity<>(staff.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Staff not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<Staff>> getStaffByPosition(@PathVariable String position) {
        List<Staff> staffList = staffService.getStaffByPosition(position);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Staff>> getStaffByDepartment(@PathVariable String department) {
        List<Staff> staffList = staffService.getStaffByDepartment(department);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Staff>> getStaffByStatus(@PathVariable String status) {
        List<Staff> staffList = staffService.getStaffByStatus(status);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStaffStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            staffService.updateStaffStatus(id, status);
            return new ResponseEntity<>("Staff status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Staff>> searchStaffByName(@RequestParam String name) {
        List<Staff> staffList = staffService.searchStaffByName(name);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @GetMapping("/search/salary")
    public ResponseEntity<List<Staff>> getStaffBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        List<Staff> staffList = staffService.getStaffBySalaryRange(minSalary, maxSalary);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
}




