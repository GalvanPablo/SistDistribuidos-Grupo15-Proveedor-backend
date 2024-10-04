package com.proveedor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveedor.entities.ItemOrdenCompra;
import com.proveedor.entities.OrdenCompra;
import com.proveedor.entities.Producto;
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

    private List<String> ordenes = new ArrayList<>();

    @KafkaListener(topics = "orden-de-compra")
    public void traerOrdenesDeCompra(String mensaje) {
        log.info(mensaje);
            ordenes.add(mensaje);

            guardarOrdenesDeCompra();
    }

    public void guardarOrdenesDeCompra() {
        for (String mensaje : ordenes) {
            try {
                OrdenCompra orden = objectMapper.readValue(mensaje, OrdenCompra.class);

                OrdenCompra ordenCompra = new OrdenCompra();
                ordenCompra.setCodigoTienda(orden.getCodigoTienda());
                ordenCompra.setFechaSolicitud(orden.getFechaSolicitud());
                ordenCompra.setEstado("SOLICITADA");

                List<ItemOrdenCompra> items = new ArrayList<>();

                for (ItemOrdenCompra item : orden.getItems()) {
                    Producto producto = productoRepository.findByCodigo(item.getProducto().getCodigo())
                            .orElseThrow(() -> new CustomException("Producti no encontrado", HttpStatus.NOT_FOUND));
                    //Color color = colorRepository.findByNombre("hoal")
                      //      .orElseThrow(() -> new CustomException("Color no encontrado", HttpStatus.NOT_FOUND)); 
                    //Talle talle = talleRepository.findByNombre("hola")
                      //      .orElseThrow(() -> new CustomException("Talle no encontrado", HttpStatus.NOT_FOUND));            
                    ItemOrdenCompra itemOrdenCompra = new ItemOrdenCompra();
                    itemOrdenCompra.setOrdenDeCompra(ordenCompra);
                    itemOrdenCompra.setProducto(producto);
                    itemOrdenCompra.setCantidad(item.getCantidad());
                    itemOrdenCompra.setColor(item.getColor());
                    itemOrdenCompra.setTalle(item.getTalle());

                    items.add(itemOrdenCompra);
                }
                ordenCompra.setItems(items);
                ordenCompraRepository.save(ordenCompra);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        }
        ordenes.clear();
    }

    
}
