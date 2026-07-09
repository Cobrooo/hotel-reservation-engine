package com.hr_engine.www.service;

import com.hr_engine.www.entity.Hotel;
import com.hr_engine.www.entity.RoomType;
import com.hr_engine.www.exception.ResourceNotFoundException;
import com.hr_engine.www.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private HotelService hotelService;

    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomType not found with id: " + id));
    }

    public RoomType createRoomType(Long hotelId, RoomType roomType) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        roomType.setHotel(hotel);
        return roomTypeRepository.save(roomType);
    }

    public RoomType updateRoomType(Long id, RoomType details) {
        RoomType roomType = getRoomTypeById(id);
        roomType.setName(details.getName());
        roomType.setBasePrice(details.getBasePrice());
        roomType.setMaxOccupancy(details.getMaxOccupancy());
        roomType.setDescription(details.getDescription());
        return roomTypeRepository.save(roomType);
    }

    public void deleteRoomType(Long id) {
        RoomType roomType = getRoomTypeById(id);
        roomTypeRepository.delete(roomType);
    }
}