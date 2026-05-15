package com.stockmanagement.hotelmanagement.guest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping
    public ResponseEntity<?> createGuest(@Valid @RequestBody Guest guest) {
        try {
            Guest createdGuest = guestService.createGuest(guest);
            return new ResponseEntity<>(createdGuest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        List<Guest> guests = guestService.getAllGuests();
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGuestById(@PathVariable Long id) {
        Optional<Guest> guest = guestService.getGuestById(id);
        if (guest.isPresent()) {
            return new ResponseEntity<>(guest.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Guest not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuest(@PathVariable Long id, @Valid @RequestBody Guest guestDetails) {
        try {
            Guest updatedGuest = guestService.updateGuest(id, guestDetails);
            if (updatedGuest != null) {
                return new ResponseEntity<>(updatedGuest, HttpStatus.OK);
            }
            return new ResponseEntity<>("Guest not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGuest(@PathVariable Long id) {
        try {
            guestService.deleteGuest(id);
            return new ResponseEntity<>("Guest deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/email")
    public ResponseEntity<?> getGuestByEmail(@RequestParam String email) {
        Optional<Guest> guest = guestService.getGuestByEmail(email);
        if (guest.isPresent()) {
            return new ResponseEntity<>(guest.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Guest not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/idnumber")
    public ResponseEntity<?> getGuestByIdNumber(@RequestParam String idNumber) {
        Optional<Guest> guest = guestService.getGuestByIdNumber(idNumber);
        if (guest.isPresent()) {
            return new ResponseEntity<>(guest.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Guest not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Guest>> searchGuestsByName(@RequestParam String name) {
        List<Guest> guests = guestService.searchGuestsByName(name);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/search/city")
    public ResponseEntity<List<Guest>> getGuestsByCity(@RequestParam String city) {
        List<Guest> guests = guestService.getGuestsByCity(city);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }

    @GetMapping("/search/country")
    public ResponseEntity<List<Guest>> getGuestsByCountry(@RequestParam String country) {
        List<Guest> guests = guestService.getGuestsByCountry(country);
        return new ResponseEntity<>(guests, HttpStatus.OK);
    }
}




