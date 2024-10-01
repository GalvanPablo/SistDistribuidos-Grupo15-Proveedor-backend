package com.proveedor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proveedor.services.DisponibilidadService;

@RestController
@RequestMapping("")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @GetMapping("/talles")
    public ResponseEntity<?> traerTalles(){
        return ResponseEntity.ok(disponibilidadService.traerTalles());
    }

    @GetMapping("/colores")
    public ResponseEntity<?> traerColores(){
        return ResponseEntity.ok(disponibilidadService.traerColores());
    }
    
}
