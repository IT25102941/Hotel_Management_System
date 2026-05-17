package com.stockmanagement.hotelmanagement.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {
    
    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room updateRoom(Long id, Room roomDetails) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setRoomNumber(roomDetails.getRoomNumber());
            existingRoom.setRoomType(roomDetails.getRoomType());
            existingRoom.setPricePerNight(roomDetails.getPricePerNight());
            existingRoom.setCapacity(roomDetails.getCapacity());
            existingRoom.setDescription(roomDetails.getDescription());
            existingRoom.setAmenities(roomDetails.getAmenities());
            existingRoom.setStatus(roomDetails.getStatus());
            existingRoom.setBedType(roomDetails.getBedType());
            existingRoom.setFloor(roomDetails.getFloor());
            return roomRepository.save(existingRoom);
        }
        return null;
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findAvailableRooms();
    }

    public List<Room> getRoomsByStatus(String status) {
        return roomRepository.findByStatus(status);
    }

    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> getRoomsByFloor(Integer floor) {
        return roomRepository.findByFloor(floor);
    }

    public Optional<Room> getRoomByRoomNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    public void updateRoomStatus(Long id, String status) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            Room existingRoom = room.get();
            existingRoom.setStatus(status);
            roomRepository.save(existingRoom);
        }
    }

    public List<Room> getAvailableRoomsByDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        // Get all available rooms
        List<Room> availableRooms = roomRepository.findAvailableRooms();
        
        // Filter out rooms that have bookings during the requested period
        return availableRooms.stream()
                .filter(room -> isRoomAvailableForDates(room.getId(), checkInDate, checkOutDate))
                .collect(Collectors.toList());
    }

    public List<Room> getAvailableRoomsBetween(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut);
    }

    private boolean isRoomAvailableForDates(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        // This would ideally use a repository method to check for conflicting bookings
        // For now, returning true - implement based on your booking repository
        return true;
    }
}




