package com.proveedor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.proveedor.dto.request.ProductoRequest;
import com.proveedor.entities.Color;
import com.proveedor.entities.Producto;
import com.proveedor.entities.Stock;
import com.proveedor.entities.Talle;
import com.proveedor.exceptions.CustomException;
import com.proveedor.repositories.IColorRepository;
import com.proveedor.repositories.IProductoRepository;
import com.proveedor.repositories.IStockRepository;
import com.proveedor.repositories.ITalleRepository;

@Service
public class ProductoService {

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IStockRepository stockRepository;

    @Autowired
    private ITalleRepository talleRepository;

    @Autowired
    private IColorRepository colorRepository;

    public Producto altaProducto(ProductoRequest request){
        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setUrl(request.getUrl());
        productoRepository.save(producto);

        for(ProductoRequest.Disponibilidad disponibilidad : request.getDisponibles()){
            Talle talle = talleRepository.findById(disponibilidad.getIdTalle()).orElseThrow(() -> new CustomException("Talle no encontrado", HttpStatus.BAD_REQUEST));
            Color color = colorRepository.findById(disponibilidad.getIdColor()).orElseThrow(() -> new CustomException("Color no encontrado", HttpStatus.BAD_REQUEST));

            Stock stock = new Stock();
            stock.setProducto(producto);
            stock.setTalle(talle);
            stock.setColor(color);
            stock.setCantidad(0);
            stockRepository.save(stock);
        }

        return producto;
    }

    
}
