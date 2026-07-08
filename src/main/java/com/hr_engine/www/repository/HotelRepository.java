package com.hr_engine.www.repository;

import com.hr_engine.www.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	
	
}