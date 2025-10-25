package com.hotel_proj.hotel.repository;


import com.hotel_proj.hotel.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
