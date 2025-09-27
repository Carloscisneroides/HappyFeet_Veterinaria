package com.happyfeet.service.dto;

import java.util.Objects;

public class UsoInsumo {
    private final Integer productoId;
    private final Integer cantidad;

    public UsoInsumo(Integer productoId, Integer cantidad) {
        this.productoId = Objects.requireNonNull(productoId);
        this.cantidad = Objects.requireNonNull(cantidad);
        if (cantidad <= 0) throw new IllegalArgumentException("cantidad debe ser > 0");
    }

    public Integer getProductoId() { return productoId; }
    public Integer getCantidad() { return cantidad; }
}
