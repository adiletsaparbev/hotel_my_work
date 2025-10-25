package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.Rooms;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Rooms, Long> {

    @EntityGraph(attributePaths = {"amenities", "images"})
    Optional<Rooms> findById(Long id);

    List<Rooms> findAllByHotel_HotelIdOrderByRoomNumber(Long id);
    Rooms findByRoomNumber(String roomNumber);

}
