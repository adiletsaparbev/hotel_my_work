package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Hotel;
import com.hotel_proj.hotel.service.HotelService;
import com.hotel_proj.hotel.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;

    public HotelController(HotelService hotelService, RoomService roomService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
    }

    @GetMapping
    public String listHotels(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        return "hotels/list";
    }

    @GetMapping("/{id}")
    public String getHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.getHotelById(id)
                .orElseThrow(() -> new IllegalArgumentException("Отель не найден"));
        model.addAttribute("hotel", hotel);
        model.addAttribute("allRooms", roomService.getAllRooms()); // для выбора уже существующих комнат
        return "hotels/details";
    }

    @GetMapping("/add")
    public String addHotelForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "hotels/form";
    }

    @PostMapping("/add")
    public String addHotel(@ModelAttribute Hotel hotel, RedirectAttributes redirectAttributes) {
        hotelService.saveHotel(hotel);
        redirectAttributes.addFlashAttribute("message", "Отель создан!");
        return "redirect:/hotels";
    }

    @PostMapping("/{id}/update")
    public String updateHotel(@PathVariable Long id, @ModelAttribute Hotel hotel, RedirectAttributes redirectAttributes) {
        hotel.setHotelId(id);
        hotelService.saveHotel(hotel);
        redirectAttributes.addFlashAttribute("message", "Отель обновлён!");
        return "redirect:/hotels/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteHotel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        hotelService.deleteHotel(id);
        redirectAttributes.addFlashAttribute("message", "Отель удалён!");
        return "redirect:/hotels";
    }

    @PostMapping("/{id}/add-room")
    public String addRoomToHotel(@PathVariable Long id, @RequestParam Long roomId, RedirectAttributes redirectAttributes) {
        hotelService.addRoomToHotel(id, roomId);
        redirectAttributes.addFlashAttribute("message", "Комната добавлена к отелю!");
        return "redirect:/hotels/" + id;
    }
    @PostMapping("/{hotelId}/remove-room/{roomId}")
    public String removeRoomFromHotel(@PathVariable Long hotelId,
                                      @PathVariable Long roomId,
                                      RedirectAttributes redirectAttributes) {
        hotelService.removeRoomFromHotel(hotelId, roomId);
        redirectAttributes.addFlashAttribute("message", "Комната удалена из отеля!");
        return "redirect:/hotels/" + hotelId;
    }

}
