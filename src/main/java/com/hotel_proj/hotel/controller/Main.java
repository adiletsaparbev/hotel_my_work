package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Booking;
import com.hotel_proj.hotel.models.Rooms;
// Предполагается, что у вас есть репозиторий для бронирований
import com.hotel_proj.hotel.repository.BookingRepository;
import com.hotel_proj.hotel.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller("/")
public class Main {

	private final RoomService roomService;
	private final BookingRepository bookingRepository; // <-- ДОБАВЛЕНО

	// Конструктор обновлен для внедрения зависимости
	public Main(RoomService roomService, BookingRepository bookingRepository) {
		this.roomService = roomService;
		this.bookingRepository = bookingRepository; // <-- ДОБАВЛЕНО
	}

	@GetMapping
	public String home(Model model) {
		model.addAttribute("rooms", roomService.getAllRooms());
		return "main/home";
	}

	// Метод детальной страницы комнаты ИЗМЕНЕН

	// 🔥 МЕТОД ИЗМЕНЕН
	@GetMapping("/{id}")
	public String roomDetail(@PathVariable("id") Long roomId, Model model) {
		Rooms room = roomService.getRoomById(roomId).orElse(null);
		if (room == null) {
			return "redirect:/";
		}

		List<Booking> bookings = bookingRepository.findByRoom_RoomId(roomId);

		// Создаем список, содержащий только даты, чтобы избежать циклической зависимости
		List<Map<String, String>> bookingDates = bookings.stream()
				.map(booking -> Map.of(
						"checkIn", booking.getCheckIn().toString(),
						"checkOut", booking.getCheckOut().toString()
				))
				.collect(Collectors.toList());

		model.addAttribute("room", room);
		// Передаем в шаблон только список с датами
		model.addAttribute("bookings", bookingDates);
		return "main/room_detail";
	}

}