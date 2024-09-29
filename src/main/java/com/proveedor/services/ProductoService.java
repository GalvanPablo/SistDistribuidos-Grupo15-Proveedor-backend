package com.proveedor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proveedor.entities.Producto;
import com.proveedor.repositories.IProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    public Producto altaProducto(Producto request){
        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setUrl(request.getUrl());
        productoRepository.save(producto);
        return producto;
    }

    
}
