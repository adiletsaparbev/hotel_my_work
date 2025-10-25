package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
