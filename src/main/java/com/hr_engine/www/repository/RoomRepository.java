package com.hr_engine.www.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr_engine.www.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	
	List<Room> findByRoomTypeId(Long roomTypeId);
	
}