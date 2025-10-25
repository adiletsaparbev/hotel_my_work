package com.hotel_proj.hotel.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roomId;
    private String roomNumber;
    private double priceNight;
    private String description;
    private int floor;
    private int capacity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room")
    @ToString.Exclude // Исключаем из toString()
    @EqualsAndHashCode.Exclude // Исключаем из equals() и hashCode()
    private List<Image> images = new ArrayList<>();

    private Long previewImageId;
    private LocalDateTime dateOfCreated;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "room_amenities",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    @ToString.Exclude // Исключаем из toString()
    @EqualsAndHashCode.Exclude // Исключаем из equals() и hashCode()
    private Set<Amenities> amenities = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "room_views",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "view_id")
    )
    @ToString.Exclude // Исключаем из toString()
    @EqualsAndHashCode.Exclude // Исключаем из equals() и hashCode()
    private Set<Views> views = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = true) // допускаем NULL
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private RoomStatus status;

    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

    public void addImageRoom(Image image){
        image.setRoom(this);
        images.add(image);
    }
}
