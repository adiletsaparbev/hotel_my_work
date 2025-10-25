package com.hotel_proj.hotel.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;
    private LocalDateTime issueDate;
    private Double totalAmount;
    private String status; // ISSUED, PAID, CANCELLED

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // getters and setters
}
