package com.proveedor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Color;

public interface IColorRepository extends JpaRepository<Color, Long>{
    
}
