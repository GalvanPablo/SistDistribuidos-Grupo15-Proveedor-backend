package com.proveedor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proveedor.dto.request.ProductoRequest;
import com.proveedor.dto.request.StockUpdateRequest;
import com.proveedor.services.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/productos")    
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/crear")
    public ResponseEntity<?> altaProducto(@RequestBody ProductoRequest request){
        return ResponseEntity.ok(productoService.altaProducto(request));
    }

    @GetMapping
    public ResponseEntity<?> traerProductos(){
        return ResponseEntity.ok(productoService.traerProductos());
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<?> traerDetalleProducto(@PathVariable Long idProducto){
        return ResponseEntity.ok(productoService.detalleProducto(idProducto));
    }

    // PARA ACTUALIZAR EL STOCK MACHEA EL ID PRODUCTO + ID TALLE + ID COLOR Y SOLO SE ACTUALIZA LA CANTIDAD
    @PutMapping("/{idProducto}/actualizar/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long idProducto, StockUpdateRequest request){
        return ResponseEntity.ok(productoService.actualizarStock(idProducto, request));
    }
    
    
}
