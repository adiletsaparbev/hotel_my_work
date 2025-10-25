package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.Rooms;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel_proj.hotel.models.Amenities;

import java.util.Optional;

public interface AmenitieRepository extends JpaRepository<Amenities, Long>  {


}
