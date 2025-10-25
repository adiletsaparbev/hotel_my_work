package com.hotel_proj.hotel.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Views {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long viewId;

    private String name; // название удобства, например "Wi-Fi", "Телевизор"

    @ManyToMany(mappedBy = "views")
    @ToString.Exclude // Исключаем из toString()
    @EqualsAndHashCode.Exclude // Исключаем из equals() и hashCode()
    private Set<Rooms> rooms = new HashSet<>();
}
