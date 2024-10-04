package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.OrdenCompra;

public interface IOrdenCompraRepository extends JpaRepository<OrdenCompra, Long>{
    
}
