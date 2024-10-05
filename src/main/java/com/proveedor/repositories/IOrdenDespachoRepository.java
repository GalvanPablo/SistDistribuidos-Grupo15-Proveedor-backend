package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.OrdenDespacho;

public interface IOrdenDespachoRepository extends JpaRepository<OrdenDespacho, Long>{
    
}
