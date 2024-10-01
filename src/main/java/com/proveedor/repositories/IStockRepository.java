package com.proveedor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Stock;

public interface IStockRepository extends JpaRepository<Stock, Long>{
    public List<Stock> findByProductoId(Long idProducto);
}
