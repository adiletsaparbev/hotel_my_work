    package com.hotel_proj.hotel.models;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDate;

    @Entity
    @Getter
    @Setter
    public class Booking {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long bookingId;

        @ManyToOne
        @JoinColumn(name = "customer_id")
        private Customer customer;

        @ManyToOne
        @JoinColumn(name = "room_id")
        private Rooms room;

        private LocalDate checkIn;
        private LocalDate checkOut;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "payment_id", referencedColumnName = "id")
        private Payment payment;

        @ManyToOne
        @JoinColumn(name = "status_id")
        private BookingStatus status;

    }
