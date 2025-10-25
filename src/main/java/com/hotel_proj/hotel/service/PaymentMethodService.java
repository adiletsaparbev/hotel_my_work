package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.PaymentMethod;
import com.hotel_proj.hotel.repository.PaymentMethodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAll();
    }

    public Optional<PaymentMethod> getById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    public PaymentMethod save(PaymentMethod method) {
        return paymentMethodRepository.save(method);
    }

    public void delete(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
