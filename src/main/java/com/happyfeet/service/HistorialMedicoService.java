package com.happyfeet.service;

import com.happyfeet.model.entities.HistorialMedico;
import com.happyfeet.service.dto.UsoInsumo;

import java.util.List;

/**
 * Servicio para registrar consultas/procedimientos médicos y aplicar reglas de negocio
 * relacionadas (por ejemplo, descuento de inventario al usar insumos o prescribir medicamentos).
 */
public interface HistorialMedicoService {

    /**
     * Registra una consulta o evento médico y descuenta automáticamente del inventario
     * los insumos/medicamentos utilizados.
     *
     * Reglas:
     * - Si un producto está vencido, se rechaza la operación.
     * - Si no hay stock suficiente, se rechaza la operación.
     * - El descuento de stock es atómico por cada ítem; si falla uno, se lanza excepción
     *   y no se persiste el historial.
     *
     * @param historial historial médico ya construido (Builder) con datos de la consulta
     * @param insumos lista de usos de insumos (productoId, cantidad)
     * @return historial guardado (con id)
     */
    HistorialMedico registrarConsultaConInsumos(HistorialMedico historial, List<UsoInsumo> insumos);
}
