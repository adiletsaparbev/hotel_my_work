package com.hotel_proj.hotel.service;


import com.hotel_proj.hotel.models.Customer;
import com.hotel_proj.hotel.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<Customer> getAll() { return repo.findAll(); }

    public Optional<Customer> getById(Long id) { return repo.findById(id); }

    public Customer save(Customer c) { return repo.save(c); }

    public void delete(Long id) { repo.deleteById(id); }
}
