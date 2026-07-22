package com.hr_engine.www.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr_engine.www.dto.HoldRequestDto;
import com.hr_engine.www.dto.PayRequestDto;
import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.service.ReservationService;

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
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<Reservation> payForReservation(
            @PathVariable Long id,
            @RequestBody PayRequestDto payRequest) {
        Reservation reservation = reservationService.processPayment(id, payRequest);
        return ResponseEntity.ok(reservation);
    }
    
}