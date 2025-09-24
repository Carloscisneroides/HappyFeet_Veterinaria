package com.happyfeet.service;

import com.happyfeet.model.entities.Dueno;

import java.util.List;
import java.util.Optional;

// Interfaz alineada con DuenoServiceImpl (no se elimina nada del Impl)
public interface DuenoService {
    Dueno crear(Dueno dueno);
    Dueno actualizar(Long id, Dueno cambios);
    void eliminar(Long id);
    Optional<Dueno> buscarPorId(Long id);
    List<Dueno> listarTodos();
    List<Dueno> buscarPorNombre(String termino);
    boolean existePorDocumento(String documento);
}
