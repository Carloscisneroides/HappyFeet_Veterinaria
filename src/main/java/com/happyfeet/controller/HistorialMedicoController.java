package com.happyfeet.controller;
import com.happyfeet.model.entities.HistorialMedico;
import com.happyfeet.service.HistorialMedicoService;
import com.happyfeet.view.HistorialMedicoView;

public class HistorialMedicoController {
    private final HistorialMedicoService service;
    private final HistorialMedicoView view;

    public HistorialMedicoController(HistorialMedicoService service, HistorialMedicoView view) {
        this.service = service;
        this.view = view;
    }

    public void registrarConsulta() {
        // Crear una consulta básica para demostración usando Builder
        HistorialMedico consulta = HistorialMedico.builder()
                .withDiagnostico("Consulta general")
                .withTratamientoPrescrito("Tratamiento básico")
                .withSintomas("Revisión de rutina")
                .build();

        service.registrarConsulta(consulta);
        view.mostrarExito("Consulta registrada exitosamente.");
    }

    public void mostrarHistorial(HistorialMedico historial) {
        view.mostrarHistorialCompleto(historial);
    }
}
