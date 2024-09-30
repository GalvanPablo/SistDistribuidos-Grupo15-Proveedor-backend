package com.proveedor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proveedor.dto.request.ProductoRequest;
import com.proveedor.services.ProductoService;


@RestController
@RequestMapping("/producto")    
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    public ResponseEntity<?> altaProducto(@RequestBody ProductoRequest request){
        System.out.println(request);
        return ResponseEntity.ok(productoService.altaProducto(request));
    }
    
}
