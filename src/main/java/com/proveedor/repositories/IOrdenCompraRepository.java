package com.proveedor.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.OrdenCompra;

public interface IOrdenCompraRepository extends JpaRepository<OrdenCompra, Long>{
    public List<OrdenCompra> findByEstado(String estado);
}
