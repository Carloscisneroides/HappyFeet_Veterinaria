package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.PuntosCliente;
import com.happyfeet.repository.PuntosClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PuntosClienteRepositoryImpl implements PuntosClienteRepository {
    private final Map<Integer, PuntosCliente> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public void save(PuntosCliente puntosCliente) {
        if (puntosCliente == null) return;
        if (puntosCliente.getId() <= 0) {
            puntosCliente.setId(idGenerator.incrementAndGet());
        }
        storage.put(puntosCliente.getId(), puntosCliente);
    }

    @Override
    public PuntosCliente findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<PuntosCliente> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(PuntosCliente puntosCliente) {
        if (puntosCliente == null || puntosCliente.getId() <= 0) return;
        if (storage.containsKey(puntosCliente.getId())) {
            storage.put(puntosCliente.getId(), puntosCliente);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }

    @Override
    public PuntosCliente findByDuenoId(Integer duenoId) {
        if (duenoId == null) return null;
        return storage.values().stream()
                .filter(puntos -> duenoId.equals(puntos.getDuenoId()))
                .findFirst()
                .orElse(null);
    }
}