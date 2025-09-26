package com.happyfeet.service.impl;

import com.happyfeet.service.LoggerManager;
import com.happyfeet.service.ReporteService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReporteServiceImpl implements ReporteService {

    private final LoggerManager logger;

    public ReporteServiceImpl(LoggerManager logger) {
        this.logger = logger;
    }

    @Override
    public void generarReporteVentasMensuales(String tipo, Map<String, Object> datos) {
        if (logger != null) {
            logger.logInfo("Generando reporte de ventas mensuales. Tipo=" + tipo);
        }
        // Implementación mínima: solo validaciones básicas y logging
        if (tipo == null || tipo.trim().isEmpty()) {
            if (logger != null) logger.logWarning("Tipo de reporte no especificado, usando 'RESUMEN'.");
            tipo = "RESUMEN";
        }
        if (datos == null) {
            if (logger != null) logger.logWarning("Datos nulos recibidos para reporte; se usará un mapa vacío.");
            datos = Collections.emptyMap();
        }
        if (logger != null) logger.logInfo("Reporte de ventas '" + tipo + "' generado con " + datos.size() + " entradas.");
    }

    @Override
    public byte[] generarReporteInventarioPdf(String idReporte) {
        if (logger != null) logger.logInfo("Generando reporte de inventario PDF: " + idReporte);
        // Implementación mínima: retornar contenido vacío
        return new byte[0];
    }

    @Override
    public byte[] generarReporteInventarioExcel(String idReporte) {
        if (logger != null) logger.logInfo("Generando reporte de inventario Excel: " + idReporte);
        // Implementación mínima: retornar contenido vacío
        return new byte[0];
    }

    @Override
    public Map<String, Object> generarReporteInventarioDatos(String idReporte) {
        if (logger != null) logger.logInfo("Generando datos de inventario para reporte: " + idReporte);
        // Implementación mínima: retornar mapa con metadatos básicos
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("idReporte", idReporte);
        resultado.put("totalItems", 0);
        resultado.put("generado", true);
        return resultado;
    }
}
