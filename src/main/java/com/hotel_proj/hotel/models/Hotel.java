package com.hotel_proj.hotel.models;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @Column(nullable = false, length = 150)
    private String name; // название отеля

    @Column(nullable = false, length = 150)
    private String city; // город

    @Column(length = 200)
    private String address; // адрес

    @Column(length = 20)
    private String phone; // телефон

    @Column(length = 255)
    private String description; // описание (например "Отель в центре города...")

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<Rooms> rooms;
}
