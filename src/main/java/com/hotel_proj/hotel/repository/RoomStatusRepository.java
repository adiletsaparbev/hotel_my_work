package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomStatusRepository extends JpaRepository<RoomStatus, Long> {
}