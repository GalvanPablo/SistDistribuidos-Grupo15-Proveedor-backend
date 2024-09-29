package com.proveedor.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proveedor.entities.Color;
import com.proveedor.services.ColorService;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping()
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping("/colores")
    public ResponseEntity<List<Color>> getColores() {
        return ResponseEntity.ok(colorService.traerColores());
    }
    
}
