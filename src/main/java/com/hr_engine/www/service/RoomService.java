package com.hr_engine.www.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr_engine.www.entity.Room;
import com.hr_engine.www.entity.RoomType;
import com.hr_engine.www.exception.ResourceNotFoundException;
import com.hr_engine.www.repository.RoomRepository;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeService roomTypeService;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    public Room createRoom(Long roomTypeId, Room room) {
        RoomType roomType = roomTypeService.getRoomTypeById(roomTypeId);
        room.setRoomType(roomType);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room details) {
        Room room = getRoomById(id);
        room.setRoomNumber(details.getRoomNumber());
        room.setFloor(details.getFloor());
        room.setStatus(details.getStatus());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }
}