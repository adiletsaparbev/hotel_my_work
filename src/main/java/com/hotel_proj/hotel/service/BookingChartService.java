package com.hotel_proj.hotel.service;
import com.hotel_proj.hotel.models.Booking;
import com.hotel_proj.hotel.models.Hotel;
import com.hotel_proj.hotel.models.Rooms;
import com.hotel_proj.hotel.repository.BookingRepository;
import com.hotel_proj.hotel.repository.HotelRepository;
import com.hotel_proj.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookingChartService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;

    public Map<String, Object> getBookingChartData(Long hotelId, LocalDate startDate, int days) {
        Map<String, Object> modelData = new HashMap<>();

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("Отель не найден с id: " + hotelId));

        List<Rooms> rooms = roomRepository.findAllByHotel_HotelIdOrderByRoomNumber(hotelId);

        // --- ВАШ КОД: Генерируем диапазон дат, начиная за 7 дней до startDate ---
        List<LocalDate> dateRange = IntStream.range(-10, days - 7)
                .mapToObj(startDate::plusDays)
                .collect(Collectors.toList());

        // --- АДАПТАЦИЯ: Определяем даты для запроса в базу на основе сгенерированного диапазона ---
        if (dateRange.isEmpty()) {
            // Если диапазон пуст, возвращаем пустые данные
            modelData.put("hotel", hotel);
            modelData.put("rooms", rooms);
            modelData.put("dateRange", Collections.emptyList());
            modelData.put("bookingMap", Collections.emptyMap());
            // ... и другие необходимые атрибуты
            return modelData;
        }


        List<Map<String, Object>> monthHeaders = new ArrayList<>();
        if (!dateRange.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("ru"));
            Map<String, Object> currentHeader = null;
            for (LocalDate d : dateRange) {
                String monthName = d.format(formatter);
                monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);

                if (currentHeader == null || !monthName.equals(currentHeader.get("name"))) {
                    currentHeader = new HashMap<>();
                    currentHeader.put("name", monthName);
                    currentHeader.put("span", 1);
                    monthHeaders.add(currentHeader);
                } else {
                    currentHeader.put("span", (int) currentHeader.get("span") + 1);
                }
            }
        }

        modelData.put("hotel", hotel);
        modelData.put("rooms", rooms);
        modelData.put("dateRange", dateRange);
        modelData.put("startDate", startDate);
        modelData.put("days", days);
        modelData.put("monthHeaders", monthHeaders);
        return modelData;
    }
}
