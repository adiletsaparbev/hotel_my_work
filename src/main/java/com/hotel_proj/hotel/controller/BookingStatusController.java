package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.BookingStatus;
import com.hotel_proj.hotel.service.BookingService;
import com.hotel_proj.hotel.service.BookingStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/booking-status")
public class BookingStatusController {
    private final BookingStatusService service;
    @Autowired
    private BookingService bookingService;

    public BookingStatusController(BookingStatusService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("statuses", service.findAll());
        model.addAttribute("status", new BookingStatus());
        return "bookings/booking-status";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BookingStatus status) {
        service.save(status);
        return "redirect:/booking-status";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/booking-status";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("statuses", service.findAll());
        model.addAttribute("status", service.findById(id));
        return "bookings/booking-status";
    }
}
