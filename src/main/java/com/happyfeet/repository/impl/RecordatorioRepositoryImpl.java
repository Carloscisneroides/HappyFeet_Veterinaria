package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Recordatorio;
import com.happyfeet.repository.RecordatorioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RecordatorioRepositoryImpl implements RecordatorioRepository {
    private final Map<Integer, Recordatorio> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public void save(Recordatorio recordatorio) {
        if (recordatorio == null) return;
        if (recordatorio.getId() <= 0) {
            recordatorio.setId(idGenerator.incrementAndGet());
        }
        storage.put(recordatorio.getId(), recordatorio);
    }

    @Override
    public Recordatorio findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Recordatorio> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Recordatorio recordatorio) {
        if (recordatorio == null || recordatorio.getId() <= 0) return;
        if (storage.containsKey(recordatorio.getId())) {
            storage.put(recordatorio.getId(), recordatorio);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}