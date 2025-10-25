package com.hotel_proj.hotel.models;

// BookingStatus.java
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // PENDING, CONFIRMED, CANCELLED

    // Один статус может принадлежать многим бронированиям
    @OneToMany(mappedBy = "status")
    private List<Booking> bookings;
}
