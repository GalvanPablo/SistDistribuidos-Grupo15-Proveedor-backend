package com.proveedor.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class ProductoRequest {

    private String codigo;
    private String nombre;
    private String url;
    private List<Disponibilidad> disponibles;
    
    @Data
    public static class Disponibilidad {
        private Long talleId;
        private Long colorId;
    }
}
