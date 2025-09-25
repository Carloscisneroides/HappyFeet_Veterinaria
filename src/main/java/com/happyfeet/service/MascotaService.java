package com.happyfeet.service;

import com.happyfeet.model.entities.Mascota;
import java.util.List;

public interface MascotaService {
    List<Mascota> buscarPorDueno(Long duenoId);
    Mascota actualizarMascota(Mascota mascota);
    // otros métodos necesarios...
}