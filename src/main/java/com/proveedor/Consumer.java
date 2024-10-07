package com.proveedor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveedor.dto.request.ItemOrdenCompraRequest;
import com.proveedor.dto.request.OrdenCompraRequest;
import com.proveedor.entities.Color;
import com.proveedor.entities.ItemOrdenCompra;
import com.proveedor.entities.OrdenCompra;
import com.proveedor.entities.OrdenDespacho;
import com.proveedor.entities.Producto;
import com.proveedor.entities.Stock;
import com.proveedor.entities.Talle;
import com.proveedor.exceptions.CustomException;
import com.proveedor.repositories.IColorRepository;
import com.proveedor.repositories.IOrdenCompraRepository;
import com.proveedor.repositories.IOrdenDespachoRepository;
import com.proveedor.repositories.IProductoRepository;
import com.proveedor.repositories.IStockRepository;
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

    @Autowired
    private IStockRepository stockRepository;

    @Autowired
    private IOrdenDespachoRepository ordenDespachoRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "orden-de-compra")
    public void traerOrdenesDeCompra(String mensaje) {
        log.info(mensaje);
        procesarOrdenDeCompra(mensaje);
    }

    public void procesarOrdenDeCompra(String mensaje) {
        try {
            OrdenCompraRequest ordenRequest = objectMapper.readValue(mensaje, OrdenCompraRequest.class);
            OrdenCompra ordenCompra = new OrdenCompra();
            ordenCompra.setId(ordenRequest.getIdOrden());
            ordenCompra.setCodigoTienda(ordenRequest.getCodigoTienda());
            ordenCompra.setFechaSolicitud(ordenRequest.getFechaSolicitud());

            List<ItemOrdenCompra> items = new ArrayList<>();
            List<String> errores = new ArrayList<>();
            List<String> faltantesStock = new ArrayList<>();

            for (ItemOrdenCompraRequest itemRequest : ordenRequest.getItems()) {
                Producto producto = productoRepository.findByCodigo(itemRequest.getCodigoProducto())
                        .orElse(null);

                if (producto == null) {
                    errores.add("Producto " + itemRequest.getCodigoProducto() + ": no existe.");
                    continue;
                }

                if (itemRequest.getCantidad() < 1) {
                    errores.add("Producto " + itemRequest.getCodigoProducto() + ": cantidad mal informada.");
                    continue;
                }

                // La consigna no pide validar colores, pero en caso de que lo pida acá
                // deberiamos dejar el orElse null y agregar el mensaje a la lista de errores
                Color color = colorRepository.findByNombre(itemRequest.getColor())
                        .orElseThrow(() -> new CustomException("Color no econtrado", HttpStatus.NOT_FOUND));
                Talle talle = talleRepository.findByNombre(itemRequest.getTalle())
                        .orElseThrow(() -> new CustomException("Talle no econtrado", HttpStatus.NOT_FOUND));

                ItemOrdenCompra itemOrdenCompra = new ItemOrdenCompra();
                itemOrdenCompra.setOrdenDeCompra(ordenCompra);
                itemOrdenCompra.setProducto(producto);
                itemOrdenCompra.setCantidad(itemRequest.getCantidad());
                itemOrdenCompra.setColor(color);
                itemOrdenCompra.setTalle(talle);

                Stock stock = stockRepository.findByProductoIdAndTalleIdAndColorId(producto.getId(), talle.getId(), color.getId())
                        .orElse(null);
                if (stock == null || stock.getCantidad() < itemRequest.getCantidad()) {
                    faltantesStock.add("Producto " + itemRequest.getCodigoProducto() + ": stock insuficiente.");
                }

                items.add(itemOrdenCompra);
            }

            ordenCompra.setItems(items);

            if (!errores.isEmpty()) {
                ordenCompra.setEstado("RECHAZADA");
                ordenCompra.setObservaciones(String.join(", ", errores));
                rechazarOrden(ordenCompra, errores);
            } else if (!faltantesStock.isEmpty()) {
                ordenCompra.setEstado("PAUSADA");
                ordenCompra.setObservaciones(String.join(", ", faltantesStock));
                pausarOrden(ordenCompra, faltantesStock);
            } else {
                ordenCompra.setEstado("ACEPTADA");
                ordenCompra.setObservaciones("Orden de compra aceptada.");
                aprobarOrdenYDespachar(ordenCompra);
            }

            ordenCompraRepository.save(ordenCompra);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    private void rechazarOrden(OrdenCompra ordenCompra, List<String> errores) {
        log.info("LLEGUE AL EMVIAR RECHAZADO");
        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("codigoTienda", ordenCompra.getCodigoTienda());
        solicitud.put("idOrdenCompra", ordenCompra.getId());
        solicitud.put("estado", "RECHAZADA");
        solicitud.put("observaciones", String.join(", ", errores));

        String mensajeSolicitud;
        try {
            mensajeSolicitud = objectMapper.writeValueAsString(solicitud);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error al procesar la orden", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        kafkaTemplate.send("solicitudes", mensajeSolicitud);
    }

    private void pausarOrden(OrdenCompra ordenCompra, List<String> faltantesStock) {
        log.info("LLEGUE AL EMVIAR ACEPTADO PERO SE PAUSA");
        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("codigoTienda", ordenCompra.getCodigoTienda());
        solicitud.put("idOrdenCompra", ordenCompra.getId());
        solicitud.put("estado", "PAUSADA");
        solicitud.put("observaciones", String.join(", ", faltantesStock));

        String mensajeSolicitud;
        try {
            mensajeSolicitud = objectMapper.writeValueAsString(solicitud);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error al procesar la orden", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        kafkaTemplate.send("solicitudes", mensajeSolicitud);
    }

    public void aprobarOrdenYDespachar(OrdenCompra ordenCompra) {
        log.info("LLEGUE AL EMVIAR ACEPTADO Y SE DESPACHA");
        OrdenDespacho ordenDespacho = new OrdenDespacho();
        Date fecha = ordenCompra.getFechaSolicitud();
        ordenDespacho.setFechaEstimadaDeEnvio(fecha);
        ordenCompra.setOrdenDeDespacho(ordenDespacho);
        ordenDespacho = ordenDespachoRepository.save(ordenDespacho);

        Map<String, Object> despacho = new HashMap<>();
        despacho.put("idOrdenDespacho", ordenDespacho.getId());
        despacho.put("idOrdenCompra", ordenCompra.getId());
        SimpleDateFormat formateo = new SimpleDateFormat("yyyy-MM-dd");
        despacho.put("fechaEstimadaEnvio", formateo.format(fecha));

        String mensajeDespacho;
        try {
            mensajeDespacho = objectMapper.writeValueAsString(despacho);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error al procesar el despacho", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        kafkaTemplate.send("despacho", mensajeDespacho);

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("codigoTienda", ordenCompra.getCodigoTienda());
        solicitud.put("estado", ordenCompra.getEstado());
        solicitud.put("idOrdenCompra", ordenCompra.getId());

        String mensajeSolicitud;
        try {
            mensajeSolicitud = objectMapper.writeValueAsString(solicitud);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error al procesar la solicitud", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        kafkaTemplate.send("solicitudes", mensajeSolicitud);
    }
}
