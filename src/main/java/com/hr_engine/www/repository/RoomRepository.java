package com.hr_engine.www.repository;

import com.hr_engine.www.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}