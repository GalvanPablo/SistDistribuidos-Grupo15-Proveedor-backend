package com.proveedor.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class OrdenCompraResponse {
    
    private Long id;
    private String estado;
    private Date fechaRecepcion;
    private Date fechaSolicitud;
    private Date fechaEstimadaEnvio;
    private String observaciones;
    private String codigoTienda;
    
}
