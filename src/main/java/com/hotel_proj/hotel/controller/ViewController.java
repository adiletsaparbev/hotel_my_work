package com.hotel_proj.hotel.controller;


import com.hotel_proj.hotel.models.Views;
import com.hotel_proj.hotel.service.ViewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;

@Controller
@RequestMapping("/views")
public class ViewController {
    private final ViewService viewService;

    public ViewController(ViewService viewService){
        this.viewService = viewService;
    }

    @GetMapping
    public String listViews(Model model){
        model.addAttribute("views", viewService.getAllViews());
        model.addAttribute("view", new Views());
        return "rooms/form_views";

    }

    @GetMapping("/add")
    public String addView(Model model){
        model.addAttribute("views", new Views());
        return "rooms/form_views";
    }

    @PostMapping("/save")
    public String saveViews(@ModelAttribute("view") Views views){
        viewService.saveView(views);
        return "redirect:/views";
    }

    @GetMapping("/edit/{id}")
    public String editView(@PathVariable Long id, Model model){
        Views views = viewService.findById(id);
        model.addAttribute("view", views);
        return "rooms/form_views";
    }

    @GetMapping("/delete/{id}")
    public String deleteView(@PathVariable Long id){
        viewService.deleteId(id);
        return "redirect:/views";
    }

}
