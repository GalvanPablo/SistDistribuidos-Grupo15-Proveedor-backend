package com.proveedor.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class OrdenCompra {
    
    @Id
    private Long id;
    
    private String codigoTienda;
    private String estado;
    private String observaciones;
    
    @Temporal(TemporalType.DATE)
    private Date fechaSolicitud;
    
    @Temporal(TemporalType.DATE)
    private Date fechaRecepcion;
    
    @OneToMany(mappedBy = "ordenDeCompra", cascade = CascadeType.ALL)
    private List<ItemOrdenCompra> items;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_despacho_id")
    private OrdenDespacho ordenDeDespacho;
}
