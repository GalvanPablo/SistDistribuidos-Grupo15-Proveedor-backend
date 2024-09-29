package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long>{
    
}
