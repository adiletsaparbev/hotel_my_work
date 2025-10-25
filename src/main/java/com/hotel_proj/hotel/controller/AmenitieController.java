package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Amenities;
import com.hotel_proj.hotel.service.AmenitieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/amenities")
public class AmenitieController {

    private final AmenitieService amenitieService;

    public AmenitieController(AmenitieService amenitieService) {
        this.amenitieService = amenitieService;
    }

    // 🔹 Показать список удобств
   @GetMapping
public String listAmenities(Model model) {
    model.addAttribute("amenities", amenitieService.findAll());
    model.addAttribute("amenity", new Amenities()); // 🔑 добавляем пустую форму
    return "rooms/form_amenities";
}

    // 🔹 Форма для добавления нового удобства
    @GetMapping("/add")
    public String addAmenityForm(Model model) {
        model.addAttribute("amenity", new Amenities());
        return "rooms/form_amenities";
    }

    // 🔹 Сохранение нового удобства
    @PostMapping("/save")
    public String saveAmenity(@ModelAttribute("amenity") Amenities amenity) {
        amenitieService.save(amenity);
        return "redirect:/amenities";
    }

    // 🔹 Форма для редактирования
    @GetMapping("/edit/{id}")
    public String editAmenity(@PathVariable Long id, Model model) {
        Amenities amenity = amenitieService.findById(id);
        model.addAttribute("amenity", amenity);
        return "rooms/form_amenities";
    }

    // 🔹 Удалить удобство
    @GetMapping("/delete/{id}")
    public String deleteAmenity(@PathVariable Long id) {
        amenitieService.deleteById(id);
        return "redirect:/amenities";
    }
}
