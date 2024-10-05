package com.proveedor.dto.response;

import lombok.Data;

@Data
public class DisponibilidadResponse {
    private Long idTalle;
    private Long idColor;
    private String talleNombre;
    private String colorNombre;
    private int cantidad;
}