package com.hotel_proj.hotel.service;

import com.hotel_proj.hotel.models.Views;
import com.hotel_proj.hotel.repository.ViewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewService {
    private final ViewRepository viewRepository;

    public  ViewService(ViewRepository viewRepository){
        this.viewRepository = viewRepository;
    }

    public Views findById(Long id){
        return viewRepository.findById(id).orElse(null);
    }

    public void saveView(Views views){
        viewRepository.save(views);
    }

    public void deleteId(Long id){
        viewRepository.deleteById(id);
    }

    public List<Views> getAllViews(){
        return viewRepository.findAll();
    }
}
