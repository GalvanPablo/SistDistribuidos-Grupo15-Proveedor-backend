package com.proveedor.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class ProductoDetalleResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String url;
    private List<DisponibilidadResponse> disponibles;
}