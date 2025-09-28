package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Sesion;
import com.happyfeet.repository.SesionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SesionRepositoryImpl implements SesionRepository {
    private final Map<Integer, Sesion> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public void save(Sesion sesion) {
        if (sesion == null) return;
        if (sesion.getId() <= 0) {
            sesion.setId(idGenerator.incrementAndGet());
        }
        storage.put(sesion.getId(), sesion);
    }

    @Override
    public Sesion findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Sesion> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Sesion sesion) {
        if (sesion == null || sesion.getId() <= 0) return;
        if (storage.containsKey(sesion.getId())) {
            storage.put(sesion.getId(), sesion);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}