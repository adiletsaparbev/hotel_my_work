package com.hotel_proj.hotel.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDateTime paymentDate;
    private String status; // PENDING, PAID, FAILED

    @ManyToOne
    @JoinColumn(name = "payment_method_id") // внешний ключ к payment_methods
    private PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
    private Invoice invoice;

    @OneToOne(mappedBy = "payment")
    private Booking booking;
}
