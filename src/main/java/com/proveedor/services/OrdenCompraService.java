package com.proveedor.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.proveedor.dto.response.ItemResponse;
import com.proveedor.dto.response.OrdenCompraResponse;
import com.proveedor.entities.OrdenCompra;
import com.proveedor.entities.Producto;
import com.proveedor.exceptions.CustomException;
import com.proveedor.repositories.IOrdenCompraRepository;

@Service
public class OrdenCompraService {

    @Autowired
    private IOrdenCompraRepository ordenCompraRepository;

    private final ModelMapper mapper = new ModelMapper();


    public List<OrdenCompraResponse> traerOrdenesDeCompra(){
        return ordenCompraRepository.findAll().stream().map(orden -> mapper.map(orden, OrdenCompraResponse.class)).collect(Collectors.toList());
    }

    public List<ItemResponse> detalleOrdenCompra(Long idOrdenCompra) {
        OrdenCompra ordenCompra = ordenCompraRepository.findById(idOrdenCompra).orElseThrow(() -> new CustomException("Orden de compra no encontrada", HttpStatus.NOT_FOUND));

        return ordenCompra.getItems().stream().map(item -> {
            Producto producto = item.getProducto();
            ItemResponse itemResponse = new ItemResponse();
            itemResponse.setNombre(producto.getNombre());
            itemResponse.setColor(item.getColor().getNombre());
            itemResponse.setTalle(item.getTalle().getNombre());
            itemResponse.setCantidad(item.getCantidad());
            return itemResponse;
        }).collect(Collectors.toList());
    }
    
}
