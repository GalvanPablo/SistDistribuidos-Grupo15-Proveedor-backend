package com.proveedor.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proveedor.entities.Color;
import com.proveedor.entities.Talle;
import com.proveedor.repositories.IColorRepository;
import com.proveedor.repositories.ITalleRepository;

@Service
public class DisponibilidadService {

    @Autowired
    private ITalleRepository talleRepository;

    @Autowired
    private IColorRepository colorRepository;


    public List<Talle> traerTalles(){
        return talleRepository.findAll();
    }
    

    public List<Color> traerColores(){
        return colorRepository.findAll();
    }
    
}
