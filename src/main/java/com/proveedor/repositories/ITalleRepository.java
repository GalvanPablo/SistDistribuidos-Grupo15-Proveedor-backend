package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Talle;

public interface ITalleRepository extends JpaRepository<Talle, Long>{
    
}
