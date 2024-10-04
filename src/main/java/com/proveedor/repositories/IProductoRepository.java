package com.proveedor.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Producto;

public interface IProductoRepository extends JpaRepository<Producto, Long>{
    public Optional<Producto> findByCodigo(String codigo);
}
