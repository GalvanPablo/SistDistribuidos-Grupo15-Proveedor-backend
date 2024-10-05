package com.proveedor.dto.request;

import lombok.Data;

@Data
public class ItemOrdenCompraRequest {

    private int cantidad;
    private String codigoProducto;
    private String color;
    private String talle;
    
}
