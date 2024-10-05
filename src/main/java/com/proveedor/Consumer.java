package com.proveedor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveedor.dto.request.ItemOrdenCompraRequest;
import com.proveedor.dto.request.OrdenCompraRequest;
import com.proveedor.entities.Color;
import com.proveedor.entities.ItemOrdenCompra;
import com.proveedor.entities.OrdenCompra;
import com.proveedor.entities.Producto;
import com.proveedor.entities.Talle;
import com.proveedor.exceptions.CustomException;
import com.proveedor.repositories.IColorRepository;
import com.proveedor.repositories.IOrdenCompraRepository;
import com.proveedor.repositories.IProductoRepository;
import com.proveedor.repositories.ITalleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Consumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IOrdenCompraRepository ordenCompraRepository;

    @Autowired
    private IProductoRepository productoRepository;

    @Autowired
    private IColorRepository colorRepository;

    @Autowired
    private ITalleRepository talleRepository;

    @KafkaListener(topics = "orden-de-compra")
    public void traerOrdenesDeCompra(String mensaje) {
        log.info(mensaje);
        guardarOrdenDeCompra(mensaje);
    }

    public void guardarOrdenDeCompra(String mensaje) {
        try {
            OrdenCompraRequest ordenDTO = objectMapper.readValue(mensaje, OrdenCompraRequest.class);

            OrdenCompra ordenCompra = new OrdenCompra();
            ordenCompra.setCodigoTienda(ordenDTO.getCodigoTienda());
            ordenCompra.setFechaSolicitud(ordenDTO.getFechaSolicitud());
            ordenCompra.setEstado("SOLICITADA");

            List<ItemOrdenCompra> items = new ArrayList<>();

            for (ItemOrdenCompraRequest itemDTO : ordenDTO.getItems()) {
                Producto producto = productoRepository.findByCodigo(itemDTO.getCodigoProducto())
                        .orElseThrow(() -> new CustomException("Producto no encontrado", HttpStatus.NOT_FOUND));

                ItemOrdenCompra itemOrdenCompra = new ItemOrdenCompra();
                itemOrdenCompra.setOrdenDeCompra(ordenCompra);
                itemOrdenCompra.setProducto(producto);
                itemOrdenCompra.setCantidad(itemDTO.getCantidad());

                if (itemDTO.getColor() != null) {
                    Color color = colorRepository.findByNombre(itemDTO.getColor())
                            .orElseThrow(() -> new CustomException("Color no encontrado", HttpStatus.NOT_FOUND));
                    itemOrdenCompra.setColor(color);
                }

                if (itemDTO.getTalle() != null) {
                    Talle talle = talleRepository.findByNombre(itemDTO.getTalle())
                            .orElseThrow(() -> new CustomException("Talle no encontrado", HttpStatus.NOT_FOUND));
                    itemOrdenCompra.setTalle(talle);
                }

                items.add(itemOrdenCompra);
            }

            ordenCompra.setItems(items);
            ordenCompraRepository.save(ordenCompra);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}