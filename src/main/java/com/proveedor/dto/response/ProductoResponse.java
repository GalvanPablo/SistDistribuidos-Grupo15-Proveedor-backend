package com.proveedor.dto.response;

import lombok.Data;

@Data
public class ProductoResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String url;
    
}
