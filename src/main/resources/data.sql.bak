INSERT INTO hotel (name, address, city, contact_info) 
VALUES ('Grand Palace Hotel', '123 MG Road', 'Visakhapatnam', 'contact@grandpalace.com');

INSERT INTO room_type (hotel_id, name, base_price, max_occupancy, description)
VALUES (1, 'Deluxe', 4500.00, 2, 'Deluxe room with sea view');

INSERT INTO room (room_type_id, room_number, floor, status)
VALUES (1, '101', 1, 'AVAILABLE');

INSERT INTO room (room_type_id, room_number, floor, status)
VALUES (1, '102', 1, 'AVAILABLE');

INSERT INTO room (room_type_id, room_number, floor, status)
VALUES (1, '103', 1, 'AVAILABLE');

-- Existing confirmed reservation for Room 101, Oct 5–10 (this is what Day 5's overlap tests will run against)
INSERT INTO reservation (room_id, check_in_date, check_out_date, status, guest_name, guest_email, created_at)
VALUES (1, '2026-10-05', '2026-10-10', 'CONFIRMED', 'Test Guest', 'testguest@example.com', NOW());