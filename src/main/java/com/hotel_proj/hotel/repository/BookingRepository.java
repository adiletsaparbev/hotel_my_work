package com.hotel_proj.hotel.repository;

import com.hotel_proj.hotel.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room.hotel.hotelId = :hotelId " +
            "AND b.checkIn <= :date " +
            "AND b.checkOut > :date")
    List<Booking> findBookingsForDate(@Param("hotelId") Long hotelId, @Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room.hotel.hotelId = :hotelId " +
            "AND b.checkIn <= :endDate " +
            "AND b.checkOut >= :startDate")
    List<Booking> findBookingsByHotelAndDateRange(@Param("hotelId") Long hotelId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.room.roomId = :roomId " +
            "AND b.status.name <> 'CANCELLED' " +
            "AND b.checkIn < :checkOut " +
            "AND b.checkOut > :checkIn")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("checkIn") LocalDate checkIn,
                                          @Param("checkOut") LocalDate checkOut);

    // Этот метод не требует аннотации @Query
    List<Booking> findByRoom_RoomId(Long roomId);
    @Query("SELECT b FROM Booking b WHERE b.room.hotel.hotelId = :hotelId")
    List<Booking> findByHotelId(@Param("hotelId") Long hotelId);


}
