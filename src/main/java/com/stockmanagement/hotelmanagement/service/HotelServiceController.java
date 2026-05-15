package com.stockmanagement.hotelmanagement.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HotelServiceController {

    @Autowired
    private HotelServiceService hotelServiceService;

    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody HotelService service) {
        try {
            HotelService createdService = hotelServiceService.createService(service);
            return new ResponseEntity<>(createdService, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<HotelService>> getAllServices() {
        List<HotelService> services = hotelServiceService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        Optional<HotelService> service = hotelServiceService.getServiceById(id);
        if (service.isPresent()) {
            return new ResponseEntity<>(service.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable Long id, @Valid @RequestBody HotelService serviceDetails) {
        try {
            HotelService updatedService = hotelServiceService.updateService(id, serviceDetails);
            if (updatedService != null) {
                return new ResponseEntity<>(updatedService, HttpStatus.OK);
            }
            return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            hotelServiceService.deleteService(id);
            return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/name")
    public ResponseEntity<?> getServiceByName(@RequestParam String serviceName) {
        Optional<HotelService> service = hotelServiceService.getServiceByName(serviceName);
        if (service.isPresent()) {
            return new ResponseEntity<>(service.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<HotelService>> getServicesByCategory(@PathVariable String category) {
        List<HotelService> services = hotelServiceService.getServicesByCategory(category);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<HotelService>> getServicesByStatus(@PathVariable String status) {
        List<HotelService> services = hotelServiceService.getServicesByStatus(status);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/provider/{provider}")
    public ResponseEntity<List<HotelService>> getServicesByProvider(@PathVariable String provider) {
        List<HotelService> services = hotelServiceService.getServicesByProvider(provider);
        return new ResponseEntity<>(services, HttpStatus.OK);
    }
}




