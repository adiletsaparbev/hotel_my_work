package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.BookingStatus;
import com.hotel_proj.hotel.repository.BookingStatusRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingStatusService {
    private final BookingStatusRepository repo;

    public BookingStatusService(BookingStatusRepository repo) {
        this.repo = repo;
    }

    public List<BookingStatus> findAll() {
        return repo.findAll();
    }

    public BookingStatus save(BookingStatus status) {
        return repo.save(status);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public BookingStatus findById(Long id) {
        return repo.findById(id).orElse(null);
    }
}
