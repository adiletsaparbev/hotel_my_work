package com.hotel_proj.hotel.controller;

import com.hotel_proj.hotel.models.Customer;
import com.hotel_proj.hotel.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) { this.service = service; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", service.getAll());
        return "customers/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer) {
        service.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Customer c = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        model.addAttribute("customer", c);
        return "customers/details";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/customers";
    }
}
