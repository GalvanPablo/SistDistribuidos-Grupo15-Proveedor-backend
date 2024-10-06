package com.proveedor.dto.request;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrdenCompraRequest {

    private Long idOrden;
    private String codigoTienda;
    private List<ItemOrdenCompraRequest> items;
    private Date fechaSolicitud;

}
