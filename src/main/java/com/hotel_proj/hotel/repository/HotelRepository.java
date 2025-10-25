package com.hotel_proj.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel_proj.hotel.models.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
