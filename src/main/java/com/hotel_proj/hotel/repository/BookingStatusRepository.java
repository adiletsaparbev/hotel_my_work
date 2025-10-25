package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, Long> {
}
