package com.happyfeet.service.impl;

import com.happyfeet.repository.InventarioRepository;
import com.happyfeet.service.InventarioService;

import java.util.Objects;

/**
 * Implementación liviana que delega en InventarioService.
 * Se removió el soporte de Observer para simplificar: las alertas no forman parte del core.
 */
public class InventarioServiceImpl {

    private final InventarioService core;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        Objects.requireNonNull(inventarioRepository, "inventarioRepository no puede ser null");
        this.core = new InventarioService(inventarioRepository);
    }

    // Mantener compatibilidad con el nombre con typo
    public void descontarStostck(Integer productoId, Integer cantidad) {
        core.descontarStostck(productoId, cantidad);
    }

    // API correcta
    public void descontarStock(Integer productoId, Integer cantidad) {
        descontarStostck(productoId, cantidad);
    }
}
