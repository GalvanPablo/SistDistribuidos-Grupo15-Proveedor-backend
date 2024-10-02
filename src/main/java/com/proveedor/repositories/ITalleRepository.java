package com.proveedor.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proveedor.entities.Talle;

public interface ITalleRepository extends JpaRepository<Talle, Long>{
    public Optional<Talle> findByNombre(String nombre);
}
