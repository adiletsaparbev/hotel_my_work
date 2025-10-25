package com.hotel_proj.hotel.service;


import com.hotel_proj.hotel.models.*;
import com.hotel_proj.hotel.repository.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository repo;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;
//      @Autowired
//     private JdbcTemplate jdbcTemplate;
//     public com.hotel_proj.hotel.models.Booking createBookingUsingProcedure(com.hotel_proj.hotel.models.Booking booking) {
//         Long roomId = booking.getRoom().getRoomId();
//         Date d1 = Date.valueOf(booking.getCheckIn());
//         Date d2 = Date.valueOf(booking.getCheckOut());

//         try {
//             // вызов процедуры: в случае конфликта она бросит SQL исключение (SIGNAL)
//             jdbcTemplate.update("CALL sp_check_room_availability(?, ?, ?)", roomId, d1, d2);
//         } catch (DataAccessException ex) {
//             // пробрасываем как runtime, контроллер обработает сообщение
//             throw new IllegalStateException(ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage(), ex);
//         }

//         // если проверка прошла — сохранение через репозиторий (существующий поток)
//         return repo.save(booking); // bookingRepository уже есть в сервисе
//     }

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private final BookingStatusRepository bookingStatusRepository;

    public BookingService(BookingRepository repo,
                          CustomerRepository customerRepository,
                          RoomRepository roomRepository,
                          PaymentRepository paymentRepository,
                          InvoiceRepository invoiceRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          BookingStatusRepository bookingStatusRepository) {
        this.repo = repo;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.bookingStatusRepository = bookingStatusRepository;
    }


    public List<Booking> getAll() { return repo.findAll(); }

    public Optional<Booking> getById(Long id) { return repo.findById(id); }

    public Booking save(Booking b)
    { return repo.save(b); }


    public Booking addRoomSave(Booking booking, Long customerId, Long roomId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        Rooms rooms = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with id: " + roomId));

        booking.setCustomer(customer);
        booking.setRoom(rooms);
        return repo.save(booking);
    }
    public Booking updateBooking(Long bookingId, Long customerId, Long roomId, Long statusId,
                                 String checkIn, String checkOut) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));

        BookingStatus status = bookingStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status not found: " + statusId));

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setStatus(status);
        booking.setCheckIn(LocalDate.parse(checkIn));
        booking.setCheckOut(LocalDate.parse(checkOut));

        return repo.save(booking);
    }


    @Transactional
    public Booking createFullBooking(Long roomId, Long paymentMethodId, Double totalAmount,
                                     String custFullName, String custEmail, String custPhone,
                                     LocalDate checkIn, LocalDate checkOut) {

        Rooms room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Комната не найдена: " + roomId));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Метод оплаты не найден: " + paymentMethodId));

        Customer customer = new Customer();
        customer.setFullName(custFullName);
        customer.setEmail(custEmail);
        customer.setPhone(custPhone);
        customerRepository.save(customer);

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("PAID");
        payment.setPaymentMethod(paymentMethod);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setTotalAmount(totalAmount);
        invoice.setStatus("PAID");
        invoice.setPayment(payment);

        // --- Вот тут меняем ---
        BookingStatus pendingStatus = bookingStatusRepository.findById(1L) // например, ID = 1 (PENDING)
                .orElseThrow(() -> new IllegalArgumentException("Default status PENDING not found"));

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomer(customer);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setStatus(pendingStatus);
        booking.setPayment(payment);

        paymentRepository.save(payment);
        invoiceRepository.save(invoice);
        return repo.save(booking);
    }


    public void delete(Long id)
    { repo.deleteById(id); }
}
