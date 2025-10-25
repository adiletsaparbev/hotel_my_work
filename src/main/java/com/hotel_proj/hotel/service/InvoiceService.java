package com.hotel_proj.hotel.service;


import com.hotel_proj.hotel.models.Invoice;
import com.hotel_proj.hotel.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }
}
