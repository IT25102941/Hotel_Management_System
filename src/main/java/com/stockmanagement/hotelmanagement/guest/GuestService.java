package com.stockmanagement.hotelmanagement.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    
    @Autowired
    private GuestRepository guestRepository;

    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public Optional<Guest> getGuestById(Long id) {
        return guestRepository.findById(id);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Guest updateGuest(Long id, Guest guestDetails) {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            Guest existingGuest = guest.get();
            existingGuest.setFirstName(guestDetails.getFirstName());
            existingGuest.setLastName(guestDetails.getLastName());
            existingGuest.setEmail(guestDetails.getEmail());
            existingGuest.setPhoneNumber(guestDetails.getPhoneNumber());
            existingGuest.setAddress(guestDetails.getAddress());
            existingGuest.setCity(guestDetails.getCity());
            existingGuest.setCountry(guestDetails.getCountry());
            existingGuest.setPostalCode(guestDetails.getPostalCode());
            existingGuest.setIdNumber(guestDetails.getIdNumber());
            existingGuest.setIdType(guestDetails.getIdType());
            existingGuest.setNotes(guestDetails.getNotes());
            return guestRepository.save(existingGuest);
        }
        return null;
    }

    public void deleteGuest(Long id) {
        guestRepository.deleteById(id);
    }

    public Optional<Guest> getGuestByEmail(String email) {
        return guestRepository.findByEmail(email);
    }

    public Optional<Guest> getGuestByIdNumber(String idNumber) {
        return guestRepository.findByIdNumber(idNumber);
    }

    public List<Guest> searchGuestsByName(String name) {
        return guestRepository.searchByName(name);
    }

    public List<Guest> getGuestsByCity(String city) {
        return guestRepository.findByCity(city);
    }

    public List<Guest> getGuestsByCountry(String country) {
        return guestRepository.findByCountry(country);
    }
}



