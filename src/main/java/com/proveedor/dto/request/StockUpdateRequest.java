package com.proveedor.dto.request;

import lombok.Data;

@Data
public class StockUpdateRequest {

    private Long idTalle;
    private Long idColor;
    private int cantidad;
    
}
