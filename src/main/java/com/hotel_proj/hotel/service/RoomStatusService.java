package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.RoomStatus;
import com.hotel_proj.hotel.repository.RoomStatusRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomStatusService {
    private final RoomStatusRepository repo;

    public RoomStatusService(RoomStatusRepository repo) {
        this.repo = repo;
    }

    public List<RoomStatus> findAll() {
        return repo.findAll();
    }

    public RoomStatus save(RoomStatus status) {
        return repo.save(status);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public RoomStatus findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
