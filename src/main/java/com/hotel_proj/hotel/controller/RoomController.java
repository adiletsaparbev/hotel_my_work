package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Rooms;
import com.hotel_proj.hotel.service.RoomService;
import com.hotel_proj.hotel.service.AmenitieService;
import com.hotel_proj.hotel.service.ViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final AmenitieService amenityService; // <-- ДОБАВЛЕНО
    private final ViewService viewService;


    public RoomController(RoomService roomService, AmenitieService amenityService, ViewService viewService) {
        this.roomService = roomService;
        this.amenityService = amenityService;
        this.viewService = viewService;
    }

    // ✅ Список всех комнат
    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms/list"; // создадим шаблон list.html
    }

    // --- МЕТОД ИЗМЕНЕН ---
    @GetMapping("/{id}")
    public String getRoomById(@PathVariable Long id, Model model) {
        Rooms room = roomService.getRoomById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));
        model.addAttribute("room", room);
        // Передаем список ВСЕХ удобств для отображения в форме
        model.addAttribute("allAmenities", amenityService.getAllAmenities());
        model.addAttribute("allViews", viewService.getAllViews());
        return "rooms/room_details";
    }

    @PostMapping("/{id}/update")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute("room") Rooms room,
                             @RequestParam(value = "amenityIds", required = false) List<Long> amenityIds,
                             @RequestParam(value = "viewIds", required = false) List<Long> viewIds,
                             @RequestParam("file1") MultipartFile file1,
                             @RequestParam("file2") MultipartFile file2,
                             @RequestParam("file3") MultipartFile file3,
                             RedirectAttributes redirectAttributes) throws IOException {

        roomService.updateRoom(id, room, amenityIds, viewIds, file1, file2, file3);
        redirectAttributes.addFlashAttribute("message", "Комната успешно обновлена.");
        return "redirect:/rooms/" + id;
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("room", new Rooms());
        model.addAttribute("allAmenities", amenityService.getAllAmenities());
        model.addAttribute("allViews", viewService.getAllViews());
        return "rooms/form";
    }

    @PostMapping("/add")
    public String addRoom(@RequestParam("file1") MultipartFile file1,
                          @RequestParam("file2") MultipartFile file2,
                          @RequestParam("file3") MultipartFile file3,
                          @RequestParam(value = "amenityIds", required = false) List<Long> amenityIds,
                          @RequestParam(value = "viewIds", required = false) List<Long> viewsIds,
                          @ModelAttribute Rooms room,
                          RedirectAttributes redirectAttributes) throws IOException {

        roomService.saveRoom(room, amenityIds, viewsIds, file1, file2, file3);
        redirectAttributes.addFlashAttribute("message", "Комната успешно добавлена.");
        return "redirect:/rooms";
    }





    // ✅ Удаление
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    // Удаление картинки
    @PostMapping("/{roomId}/images/{imageId}/delete")
    public String deleteImage(@PathVariable Long roomId,
                              @PathVariable Long imageId,
                              RedirectAttributes redirectAttributes) {
        roomService.deleteImage(roomId, imageId);
        redirectAttributes.addFlashAttribute("message", "Фото удалено.");
        return "redirect:/rooms/" + roomId;
    }

    // Замена картинки
    @PostMapping("/{roomId}/images/{imageId}/replace")
    public String replaceImage(@PathVariable Long roomId,
                               @PathVariable Long imageId,
                               @RequestParam("newImage") MultipartFile newImage,
                               RedirectAttributes redirectAttributes) throws IOException {
        roomService.replaceImage(roomId, imageId, newImage);
        redirectAttributes.addFlashAttribute("message", "Фото заменено.");
        return "redirect:/rooms/" + roomId;
    }

}
