package com.hr_engine.www;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotelReservationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelReservationEngineApplication.class, args);
	}

}
