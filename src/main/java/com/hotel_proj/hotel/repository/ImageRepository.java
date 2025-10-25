package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
