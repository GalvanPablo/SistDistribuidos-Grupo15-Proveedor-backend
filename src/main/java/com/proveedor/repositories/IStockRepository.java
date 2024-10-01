package com.proveedor.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Stock;

public interface IStockRepository extends JpaRepository<Stock, Long>{
    public List<Stock> findByProductoId(Long idProducto);
    public Optional<Stock> findByProductoIdAndTalleIdAndColorId(Long idProducto, Long idTalle, Long idColor);
}
