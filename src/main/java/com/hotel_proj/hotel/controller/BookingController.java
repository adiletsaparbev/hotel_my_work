package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Booking;
import com.hotel_proj.hotel.models.Rooms;
import com.hotel_proj.hotel.repository.PaymentMethodRepository;
import com.hotel_proj.hotel.service.BookingService;
import com.hotel_proj.hotel.service.CustomerService;
import com.hotel_proj.hotel.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;

    private final PaymentMethodRepository paymentMethodRepository; // Добавляем

    // Обновляем конструктор
    public BookingController(BookingService bookingService, CustomerService customerService, RoomService roomService, PaymentMethodRepository paymentMethodRepository) {
        this.bookingService = bookingService;
        this.customerService = customerService;
        this.roomService = roomService;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bookings", bookingService.getAll());
        return "bookings/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Booking booking,
                       @RequestParam Long roomId,
                       @RequestParam Long customerId) {
        bookingService.addRoomSave(booking, customerId, roomId);
        return "redirect:/bookings";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Booking b = bookingService.getById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        model.addAttribute("booking", b);
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "bookings/details";
    }


    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @RequestParam Long customerId,
                                @RequestParam Long roomId,
                                @RequestParam Long status,
                                @RequestParam String checkIn,
                                @RequestParam String checkOut) {
        bookingService.updateBooking(id, customerId, roomId, status, checkIn, checkOut);
        return "redirect:/bookings/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookingService.delete(id);
        return "redirect:/bookings";
    }



    @GetMapping("/new")
    public String showBookingForm(@RequestParam("roomId") Long roomId,
                                  @RequestParam("checkIn") String checkIn,
                                  @RequestParam("checkOut") String checkOut,
                                  Model model) {
        Rooms room = roomService.getRoomById(roomId).orElseThrow(() -> new IllegalArgumentException("Комната не найдена"));
        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        // Рассчитываем количество ночей и итоговую стоимость
        long totalNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double totalPrice = totalNights * room.getPriceNight();

        model.addAttribute("room", room);
        model.addAttribute("checkIn", checkIn);
        model.addAttribute("checkOut", checkOut);
        model.addAttribute("totalNights", totalNights);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentMethods", paymentMethodRepository.findAll());

        return "bookings/form_customer"; // Возвращаем новую страницу
    }

    /**
     * Обрабатывает отправку формы и создаёт полное бронирование.
     */
    @PostMapping("/finalize")
    public String finalizeBooking(@RequestParam Long roomId,
                                  @RequestParam String checkIn,
                                  @RequestParam String checkOut,
                                  @RequestParam Double totalAmount,
                                  @RequestParam String fullName,
                                  @RequestParam String email,
                                  @RequestParam String phone,
                                  @RequestParam Long paymentMethodId,
                                  RedirectAttributes redirectAttributes) {

        bookingService.createFullBooking(roomId, paymentMethodId, totalAmount, fullName, email, phone,
                LocalDate.parse(checkIn), LocalDate.parse(checkOut));

        redirectAttributes.addFlashAttribute("successMessage", "Ваше бронирование успешно завершено!");
        return "redirect:/"; // Перенаправляем на главную страницу
    }

    // @GetMapping("/bookings/add")
    // public String showAddForm(Model model) {
    //     model.addAttribute("booking", new Booking());
    //     return "bookings/bookings_add";
    // }

    // @PostMapping("/proc-create")
    // public String createBookingViaProcedure(@ModelAttribute("booking") com.hotel_proj.hotel.models.Booking booking,
    //                                         RedirectAttributes redirectAttributes) {
    //     try {
    //         com.hotel_proj.hotel.models.Booking saved = bookingService.createBookingUsingProcedure(booking);
    //         redirectAttributes.addFlashAttribute("message", "Бронирование создано (id=" + saved.getBookingId() + ")");
    //         return "redirect:/bookings/" + saved.getBookingId();
    //     } catch (Exception ex) {
    //         // Передаём ошибку назад на форму
    //         redirectAttributes.addFlashAttribute("error", ex.getMessage());
    //         return "redirect:/bookings/bookings/add"; // предполагается существующая форма
    //     }
    // }
}
