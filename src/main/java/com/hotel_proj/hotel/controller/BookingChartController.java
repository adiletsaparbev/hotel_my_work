package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Booking;
import com.hotel_proj.hotel.repository.BookingRepository;
import com.hotel_proj.hotel.service.BookingChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/booking-chart")
@RequiredArgsConstructor
public class BookingChartController {

    private final BookingChartService bookingChartService;
    private final BookingRepository bookingRepository;

    @GetMapping("/{hotelId}")
    public String showBookingChart(@PathVariable("hotelId") Long hotelId,
                                   @RequestParam(required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(defaultValue = "30") int days,
                                   Model model) {

        LocalDate date = (startDate == null) ? LocalDate.now() : startDate;

        List<Booking> bookings = bookingRepository.findByHotelId(hotelId);
        Map<String, Object> chartData = bookingChartService.getBookingChartData(hotelId, date, days);
        model.addAllAttributes(chartData);
        model.addAttribute("bookings", bookings);

        return "chart_booking/booking_chart_1";
    }
}
