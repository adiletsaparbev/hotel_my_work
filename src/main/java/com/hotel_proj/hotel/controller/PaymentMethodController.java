package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.PaymentMethod;
import com.hotel_proj.hotel.service.PaymentMethodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    // Список всех методов
    @GetMapping
    public String list(Model model) {
        model.addAttribute("methods", paymentMethodService.getAll());
        model.addAttribute("paymentMethod", new PaymentMethod());
        return "payment/payment_method_form"; // сделаем таблицу
    }

    // Форма добавления
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("paymentMethod", new PaymentMethod());
        return "payment/payment_method_form";
    }

    // Сохранение (и для add, и для edit)
    @PostMapping("/save")
    public String save(@ModelAttribute PaymentMethod paymentMethod) {
        paymentMethodService.save(paymentMethod);
        return "redirect:/payment-methods";
    }

    // Форма редактирования
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        PaymentMethod method = paymentMethodService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method Id:" + id));
        model.addAttribute("paymentMethod", method);
        return "payment/payment_method_form";
    }

    // Удаление
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        paymentMethodService.delete(id);
        return "redirect:/payment-methods";
    }
}
