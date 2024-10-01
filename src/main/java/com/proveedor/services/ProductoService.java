package com.proveedor.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.proveedor.dto.request.ProductoRequest;
import com.proveedor.dto.response.DisponibilidadResponse;
import com.proveedor.dto.response.ProductoDetalleResponse;
import com.proveedor.dto.response.ProductoResponse;
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

    private final ModelMapper mapper = new ModelMapper();

    public ProductoResponse altaProducto(ProductoRequest request){
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

        ProductoResponse response = mapper.map(producto, ProductoResponse.class);
        return response;
    }

    public List<ProductoResponse> traerProductos(){
        return productoRepository.findAll().stream().map(producto -> mapper.map(producto, ProductoResponse.class)).collect(Collectors.toList());
    }

    public ProductoDetalleResponse detalleProducto(Long idProducto){
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new CustomException("Producto no encontrado", HttpStatus.NOT_FOUND));
        
        List<Stock> stocks = stockRepository.findByProductoId(idProducto);
        ProductoDetalleResponse response = mapper.map(producto, ProductoDetalleResponse.class);

        List<DisponibilidadResponse> disponibilidades = stocks.stream().map(stock -> {
            DisponibilidadResponse disponibilidad = new DisponibilidadResponse();
            disponibilidad.setIdTalle(stock.getTalle().getId());
            disponibilidad.setIdColor(stock.getColor().getId());
            disponibilidad.setCantidad(stock.getCantidad());
            return disponibilidad;
        }).collect(Collectors.toList());

        response.setDisponibles(disponibilidades);

        return response;
    }

    
}
