//package com.hotel_proj.hotel.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//@Getter
//@AllArgsConstructor
//public class BookingInfo {
//
//    // Тип сегмента бронирования для корректного отображения
//    public enum SegmentType {
//        START,  // День заезда (правая половина)
//        MIDDLE, // Промежуточный день (вся ячейка)
//        END,    // День выезда (левая половина)
//        SINGLE  // Бронь на одну ночь (объединяет START и END)
//    }
//
//    private final Long bookingId;
//    private final String customerName;
//    private final String status; // e.g., "CONFIRMED", "PENDING"
//    private final SegmentType segmentType;
//}
