package com.hr_engine.www.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hr_engine.www.entity.Hotel;
import com.hr_engine.www.entity.Reservation;
import com.hr_engine.www.entity.Room;
import com.hr_engine.www.entity.RoomType;
import com.hr_engine.www.enums.ReservationStatus;
import com.hr_engine.www.enums.RoomStatus;
import com.hr_engine.www.repository.HotelRepository;
import com.hr_engine.www.repository.ReservationRepository;
import com.hr_engine.www.repository.RoomRepository;
import com.hr_engine.www.repository.RoomTypeRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(HotelRepository hotelRepo, RoomTypeRepository roomTypeRepo,
                                RoomRepository roomRepo, ReservationRepository reservationRepo) {
        return args -> {
            if (hotelRepo.count() > 0) return; // avoid re-seeding on every restart

            Hotel hotel = new Hotel(null, "Grand Palace Hotel", "123 MG Road", "Visakhapatnam", "contact@grandpalace.com", null);
            hotel = hotelRepo.save(hotel);

            RoomType deluxe = new RoomType(hotel, "Deluxe", new BigDecimal("4500.00"), 2, "Deluxe room with sea view");
            deluxe = roomTypeRepo.save(deluxe);

            Room room101 = roomRepo.save(new Room(deluxe, "101", 1, RoomStatus.AVAILABLE));
            roomRepo.save(new Room(deluxe, "102", 1, RoomStatus.AVAILABLE));
            roomRepo.save(new Room(deluxe, "103", 1, RoomStatus.AVAILABLE));

            reservationRepo.save(new Reservation(room101, LocalDate.of(2026,10,5), LocalDate.of(2026,10,10),
                    ReservationStatus.CONFIRMED, "Test Guest", "testguest@example.com"));

            System.out.println("Test data seeded successfully.");
        };
    }
    
}