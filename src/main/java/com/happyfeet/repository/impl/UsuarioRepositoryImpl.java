package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Usuario;
import com.happyfeet.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final Map<Integer, Usuario> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public void save(Usuario usuario) {
        if (usuario == null) return;
        if (usuario.getId() <= 0) {
            usuario.setId(idGenerator.incrementAndGet());
        }
        storage.put(usuario.getId(), usuario);
    }

    @Override
    public Usuario findById(int id) {
        return storage.get(id);
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Usuario usuario) {
        if (usuario == null || usuario.getId() <= 0) return;
        if (storage.containsKey(usuario.getId())) {
            storage.put(usuario.getId(), usuario);
        }
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}