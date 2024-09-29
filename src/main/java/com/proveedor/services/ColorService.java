package com.proveedor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proveedor.entities.Color;
import com.proveedor.repositories.IColorRepository;

@Service
public class ColorService {
    
    @Autowired
    private IColorRepository colorRepository;

    public List<Color> traerColores(){
        return colorRepository.findAll();
    }

}
