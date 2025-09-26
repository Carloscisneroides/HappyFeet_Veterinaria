package com.happyfeet.service.impl;

import com.happyfeet.model.entities.Factura;
import com.happyfeet.model.entities.Inventario;
import com.happyfeet.model.entities.Servicio;
import com.happyfeet.repository.FacturaRepository;
import com.happyfeet.service.FacturaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio de facturación que orquesta la creación, modificación y cálculo de totales
 * delegando las reglas numéricas a la entidad Factura (usa BigDecimal internamente).
 */
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository repository;

    public FacturaServiceImpl(FacturaRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository no puede ser null");
    }

    @Override
    public Factura crearFactura(Factura factura) {
        Objects.requireNonNull(factura, "factura no puede ser null");
        // No invocar métodos privados de Factura; se asume que las operaciones de agregado ya recalculan.
        return repository.save(factura);
    }

    @Override
    public Factura crearFacturaConItems(Factura factura, List<Factura.ItemFactura> items) {
        Objects.requireNonNull(factura, "factura no puede ser null");
        Objects.requireNonNull(items, "items no puede ser null");
        for (Factura.ItemFactura item : items) {
            factura.agregarItem(item); // agregarItem ya recalcula totales
        }
        return repository.save(factura);
    }

    @Override
    public Optional<Factura> obtenerPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Factura> listarTodas() {
        return repository.findAll();
    }

    @Override
    public boolean eliminar(Integer id) {
        return repository.deleteById(id);
    }

    @Override
    public Factura agregarServicio(Factura factura, Servicio servicio) {
        Objects.requireNonNull(factura, "factura no puede ser null");
        Objects.requireNonNull(servicio, "servicio no puede ser null");
        factura.agregarServicio(servicio); // ya recalcula totales internamente
        return repository.save(factura);
    }

    @Override
    public Factura agregarProducto(Factura factura, Inventario producto, BigDecimal cantidad) {
        Objects.requireNonNull(factura, "factura no puede ser null");
        Objects.requireNonNull(producto, "producto no puede ser null");
        Objects.requireNonNull(cantidad, "cantidad no puede ser null");
        factura.agregarProducto(producto, cantidad); // ya recalcula totales internamente
        return repository.save(factura);
    }

    @Override
    public Factura recalcularTotales(Factura factura) {
        Objects.requireNonNull(factura, "factura no puede ser null");
        // Forzamos un recálculo usando una API pública
        factura.aplicarDescuento(factura.getDescuento());
        return repository.save(factura);
    }
}
