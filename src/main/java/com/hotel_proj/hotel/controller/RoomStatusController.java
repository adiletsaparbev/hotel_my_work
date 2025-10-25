package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.RoomStatus;
import com.hotel_proj.hotel.service.RoomStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/room-status")
public class RoomStatusController {
    private final RoomStatusService service;

    public RoomStatusController(RoomStatusService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("statuses", service.findAll());
        model.addAttribute("status", new RoomStatus());
        return "rooms/room-status";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute RoomStatus status) {
        service.save(status);
        return "redirect:/room-status";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/room-status";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("statuses", service.findAll());
        model.addAttribute("status", service.findById(id));
        return "rooms/room-status";
    }
}
