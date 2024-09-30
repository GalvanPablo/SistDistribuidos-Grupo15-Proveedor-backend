package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Stock;

public interface IStockRepository extends JpaRepository<Stock, Long>{
    
}
