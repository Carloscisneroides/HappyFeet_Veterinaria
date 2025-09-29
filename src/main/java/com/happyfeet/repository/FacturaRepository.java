package com.happyfeet.repository;

import com.happyfeet.model.entities.Factura;

import java.util.List;
import java.util.Optional;

public interface FacturaRepository {
    Factura save(Factura factura);
    Optional<Factura> findById(Integer id);
    List<Factura> findAll();
    boolean deleteById(Integer id);
}
