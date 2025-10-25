package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.Amenities;
import org.springframework.stereotype.Service;
import com.hotel_proj.hotel.repository.AmenitieRepository;

import java.util.List;

@Service
public class AmenitieService {

    private final AmenitieRepository amenitieRepository;

    public AmenitieService(AmenitieRepository amenitieRepository) {
        this.amenitieRepository = amenitieRepository;
    }

    public List<Amenities> findAll() {
        return amenitieRepository.findAll();
    }

    public Amenities findById(Long id) {
        return amenitieRepository.findById(id).orElse(null);
    }

    public void save(Amenities amenity) {
        amenitieRepository.save(amenity);
    }

    public void deleteById(Long id) {
        amenitieRepository.deleteById(id);
    }
    public List<Amenities> getAllAmenities() {
        return amenitieRepository.findAll();
    }
}
