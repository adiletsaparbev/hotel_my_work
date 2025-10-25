package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

}