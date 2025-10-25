package com.hotel_proj.hotel.repository;


import com.hotel_proj.hotel.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}