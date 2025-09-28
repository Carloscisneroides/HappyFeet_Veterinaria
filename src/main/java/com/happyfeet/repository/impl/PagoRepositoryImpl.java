package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Pago;
import com.happyfeet.repository.PagoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PagoRepositoryImpl implements PagoRepository {
    private final Map<Integer, Pago> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public void save(Pago pago) {
        if (pago == null) return;
        if (pago.getId() <= 0) {
            pago.setId(idGenerator.incrementAndGet());
        }
        storage.put(pago.getId(), pago);
    }

    @Override
    public Pago findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Pago> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Pago pago) {
        if (pago == null || pago.getId() <= 0) return;
        if (storage.containsKey(pago.getId())) {
            storage.put(pago.getId(), pago);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
