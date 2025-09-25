package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Factura;
import com.happyfeet.repository.FacturaRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementación en memoria de FacturaRepository para propósitos de demo/pruebas.
 */
public class FacturaRepositoryImpl implements FacturaRepository {

    private final Map<Integer, Factura> storage = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @Override
    public Factura save(Factura factura) {
        if (factura.getId() == null) {
            factura.setId(sequence.incrementAndGet());
        }
        storage.put(factura.getId(), factura);
        return factura;
    }

    @Override
    public Optional<Factura> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Factura> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(Integer id) {
        return storage.remove(id) != null;
    }
}
