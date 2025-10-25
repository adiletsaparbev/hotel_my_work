package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.Hotel;
import com.hotel_proj.hotel.models.Rooms;
import com.hotel_proj.hotel.repository.HotelRepository;
import com.hotel_proj.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository; // нужно для добавления комнат

    public HotelService(HotelRepository hotelRepository, RoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    public void addRoomToHotel(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Отель не найден"));
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));

        room.setHotel(hotel);
        roomRepository.save(room);
    }

    @Transactional
    public void removeRoomFromHotel(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Отель не найден"));
        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));

        if (room.getHotel() != null && room.getHotel().equals(hotel)) {
            room.setHotel(null); // убираем связь
            roomRepository.save(room);
        }
    }

}
