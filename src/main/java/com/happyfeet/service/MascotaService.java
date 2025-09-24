package com.happyfeet.service;

import com.happyfeet.model.entities.Mascota;

import java.util.List;
import java.util.Optional;

public interface MascotaService {
    Mascota crear(Mascota mascota);
    Mascota actualizar(Long id, Mascota cambios);
    void eliminar(Long id);
    Optional<Mascota> buscarPorId(Long id);
    List<Mascota> listarTodos();

    List<Mascota> listarPorDueno(Long duenoId);
    List<Mascota> buscarPorNombre(String termino);
    boolean existePorMicrochip(String microchip);
}
