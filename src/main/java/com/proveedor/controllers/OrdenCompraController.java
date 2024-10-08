package com.proveedor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.proveedor.services.OrdenCompraService;

@RestController
@RequestMapping("/ordenes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;


    @GetMapping
    public ResponseEntity<?> traerOrdenesCompra(){
        return ResponseEntity.ok(ordenCompraService.traerOrdenesDeCompra());
    }

    @GetMapping("/{idOrdenCompra}")
    public ResponseEntity<?> detalleOrdenCompra(@PathVariable Long idOrdenCompra){
        return ResponseEntity.ok(ordenCompraService.detalleOrdenCompra(idOrdenCompra));
    }
    
}
