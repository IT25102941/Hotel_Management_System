package com.stockmanagement.hotelmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceService {
    
    @Autowired
    private HotelServiceRepository hotelServiceRepository;

    public HotelService createService(HotelService service) {
        return hotelServiceRepository.save(service);
    }

    public Optional<HotelService> getServiceById(Long id) {
        return hotelServiceRepository.findById(id);
    }

    public List<HotelService> getAllServices() {
        return hotelServiceRepository.findAll();
    }

    public HotelService updateService(Long id, HotelService serviceDetails) {
        Optional<HotelService> service = hotelServiceRepository.findById(id);
        if (service.isPresent()) {
            HotelService existingService = service.get();
            existingService.setServiceName(serviceDetails.getServiceName());
            existingService.setDescription(serviceDetails.getDescription());
            existingService.setCategory(serviceDetails.getCategory());
            existingService.setPrice(serviceDetails.getPrice());
            existingService.setStatus(serviceDetails.getStatus());
            existingService.setAvailability(serviceDetails.getAvailability());
            existingService.setProvider(serviceDetails.getProvider());
            existingService.setRequiresAdvanceBooking(serviceDetails.getRequiresAdvanceBooking());
            existingService.setMaxCapacity(serviceDetails.getMaxCapacity());
            existingService.setContactInfo(serviceDetails.getContactInfo());
            return hotelServiceRepository.save(existingService);
        }
        return null;
    }

    public void deleteService(Long id) {
        hotelServiceRepository.deleteById(id);
    }

    public Optional<HotelService> getServiceByName(String serviceName) {
        return hotelServiceRepository.findByServiceName(serviceName);
    }

    public List<HotelService> getServicesByCategory(String category) {
        return hotelServiceRepository.findByCategory(category);
    }

    public List<HotelService> getServicesByStatus(String status) {
        return hotelServiceRepository.findByStatus(status);
    }

    public List<HotelService> getServicesByProvider(String provider) {
        return hotelServiceRepository.findByProvider(provider);
    }
}




