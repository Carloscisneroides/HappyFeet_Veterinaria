package com.happyfeet.service;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.InventarioRepository;

public class InventarioService {
    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    // Se mantiene el nombre del método tal como está en tu código para no romper llamadas existentes
    public void descontarStostck(Integer productoId, Integer cantidad) {
        Inventario producto = inventarioRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        // Las siguientes llamadas asumen que tu entidad Inventario cuenta con estos métodos de dominio.
        // Si aún no existen, ver notas más abajo.
        if (producto.estaVencido()) {
            throw new IllegalStateException("No se puede vender un producto vencido: " + producto.getNombreProducto());
        }
        if (!producto.tieneStock(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombreProducto());
        }

        producto.descontarStock(cantidad);
        inventarioRepository.update(producto);
    }
}