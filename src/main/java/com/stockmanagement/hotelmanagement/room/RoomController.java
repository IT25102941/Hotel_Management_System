package com.stockmanagement.hotelmanagement.room;

import com.stockmanagement.hotelmanagement.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room room) {
        try {
            Room createdRoom = roomService.createRoom(room);
            return new ResponseEntity<>(createdRoom, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomService.getRoomById(id);
        if (room.isPresent()) {
            return new ResponseEntity<>(room.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @Valid @RequestBody Room roomDetails) {
        try {
            Room updatedRoom = roomService.updateRoom(id, roomDetails);
            if (updatedRoom != null) {
                return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
            }
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return new ResponseEntity<>("Room deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/available/list")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = roomService.getAvailableRooms();
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }

    @GetMapping("/available/by-dates")
    public ResponseEntity<?> getAvailableRoomsByDateRange(
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {
        try {
            List<Room> availableRooms = roomService.getAvailableRoomsByDateRange(
                    LocalDate.parse(checkInDate),
                    LocalDate.parse(checkOutDate)
            );
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Available rooms for date range retrieved", availableRooms),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Error retrieving available rooms", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/available-between")
    public ResponseEntity<?> getAvailableRoomsBetween(
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        try {
            if (checkIn == null || checkOut == null) {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "Check-in and check-out dates are required", null),
                        HttpStatus.BAD_REQUEST
                );
            }
            if (checkIn.isAfter(checkOut) || checkIn.isEqual(checkOut)) {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "Check-out date must be after check-in date", null),
                        HttpStatus.BAD_REQUEST
                );
            }
            List<Room> availableRooms = roomService.getAvailableRoomsBetween(checkIn, checkOut);
            return new ResponseEntity<>(
                    new ApiResponse<>(true, "Available rooms retrieved successfully", availableRooms),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "Error retrieving available rooms", null, e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Room>> getRoomsByStatus(@PathVariable String status) {
        List<Room> rooms = roomService.getRoomsByStatus(status);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<Room>> getRoomsByType(@PathVariable String roomType) {
        List<Room> rooms = roomService.getRoomsByType(roomType);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Room>> getRoomsByFloor(@PathVariable Integer floor) {
        List<Room> rooms = roomService.getRoomsByFloor(floor);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateRoomStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            roomService.updateRoomStatus(id, status);
            return new ResponseEntity<>("Room status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}




