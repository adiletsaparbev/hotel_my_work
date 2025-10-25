package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Payment;
import com.hotel_proj.hotel.models.PaymentMethod;
import com.hotel_proj.hotel.service.PaymentMethodService;
import com.hotel_proj.hotel.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMethodService paymentMethodService;

    public PaymentController(PaymentService paymentService, PaymentMethodService paymentMethodService) {
        this.paymentService = paymentService;
        this.paymentMethodService = paymentMethodService;
    }

    // Список и форма на одной странице
    @GetMapping
    public String list(Model model) {
        model.addAttribute("payments", paymentService.findAll());
        model.addAttribute("paymentMethods", paymentMethodService.getAll());
        model.addAttribute("payment", new Payment()); // для формы добавления
        return "payment/payment_form";
    }

    // Сохранение нового или редактирование
    @PostMapping("/save")
    public String save(@ModelAttribute Payment payment, @RequestParam Long paymentMethodId) {
        PaymentMethod method = paymentMethodService.getById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method Id:" + paymentMethodId));
        payment.setPaymentMethod(method);
        if(payment.getPaymentDate() == null){
            payment.setPaymentDate(LocalDateTime.now());
        }
        paymentService.save(payment);
        return "redirect:/payments";
    }

    // Форма редактирования
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Payment payment = paymentService.findById(id);
        model.addAttribute("payment", payment);
        model.addAttribute("payments", paymentService.findAll());
        model.addAttribute("paymentMethods", paymentMethodService.getAll());
        return "payment/payment_form";
    }

    // Удаление
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        paymentService.delete(id);
        return "redirect:/payments";
    }
}
