package com.hr_engine.www.controller;

import com.hr_engine.www.dto.HoldRequestDto;
import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/hold")
    public ResponseEntity<Reservation> holdRoom(@RequestBody HoldRequestDto request) {
        Reservation reservation = reservationService.holdRoom(request);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }
    
}