package com.hotel_proj.hotel.models;

// RoomStatus.java
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // AVAILABLE, OCCUPIED, CLEANING, REPAIR

    // Один статус может принадлежать многим комнатам
    @OneToMany(mappedBy = "status")
    private List<Rooms> rooms;
}
