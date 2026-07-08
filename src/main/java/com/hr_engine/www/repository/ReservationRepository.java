package com.hr_engine.www.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr_engine.www.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	
	
}
