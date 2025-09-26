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
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser un entero positivo");
        }
        Inventario producto = inventarioRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        if (producto.estaVencido()) {
            throw new IllegalStateException("No se puede vender un producto vencido: " + producto.getNombreProducto());
        }
        if (!producto.tieneStock(cantidad)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombreProducto());
        }

        producto.descontarStock(cantidad);
        inventarioRepository.update(producto);
    }

    // Nueva API con el nombre correcto, manteniendo compatibilidad hacia atrás
    public void descontarStock(Integer productoId, Integer cantidad) {
        descontarStostck(productoId, cantidad);
    }
}