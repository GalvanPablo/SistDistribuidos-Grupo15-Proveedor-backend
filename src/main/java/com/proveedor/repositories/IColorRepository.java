package com.proveedor.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Color;

public interface IColorRepository extends JpaRepository<Color, Long>{
    public Optional<Color> findByNombre(String nombre);
}
