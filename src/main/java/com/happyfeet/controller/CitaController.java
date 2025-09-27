package com.happyfeet.controller;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.enums.CitaEstado;
import com.happyfeet.service.CitaService;
import com.happyfeet.service.MascotaService;
import com.happyfeet.service.VeterinarioService;
import com.happyfeet.view.CitaView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class CitaController {

    private final CitaService citaService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;
    private final CitaView citaView;

    public CitaController(CitaService citaService, MascotaService mascotaService,
                          VeterinarioService veterinarioService, CitaView citaView) {
        this.citaService = citaService;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
        this.citaView = citaView;
    }

    public void crearCita(Long idVeterinario, Long idMascota, LocalDateTime inicio,
                          LocalDateTime fin, String motivo) {
        try {
            // Validaciones básicas
            if (inicio.isBefore(LocalDateTime.now())) {
                citaView.mostrarError("No se pueden crear citas en fechas pasadas");
                return;
            }

            if (fin != null && fin.isBefore(inicio)) {
                citaView.mostrarError("La hora de fin no puede ser anterior al inicio");
                return;
            }

            // Verificar disponibilidad del veterinario
            if (citaService.haySolape(idVeterinario, inicio, fin)) {
                citaView.mostrarError("El veterinario ya tiene una cita en ese horario");
                return;
            }

            Cita nuevaCita = new Cita();
            nuevaCita.setIdVeterinario(idVeterinario);
            nuevaCita.setIdMascota(idMascota);
            nuevaCita.setInicio(inicio);
            nuevaCita.setFin(fin);
            nuevaCita.setMotivo(motivo);
            // No establecer estado explícito aquí; el servicio asigna el estado inicial adecuado

            Cita citaCreada = citaService.crear(nuevaCita);
            citaView.mostrarMensaje("Cita creada exitosamente para: " +
                    citaView.formatearFechaHora(inicio));

        } catch (Exception e) {
            citaView.mostrarError("Error al crear cita: " + e.getMessage());
        }
    }


    public Optional<Cita> buscarPorId(Long id) {
        try {
            Optional<Cita> cita = citaService.buscarPorId(id);
            if (cita.isEmpty()) {
                citaView.mostrarMensaje("No se encontró cita con ID: " + id);
            }
            return cita;
        } catch (Exception e) {
            citaView.mostrarError("Error al buscar cita: " + e.getMessage());
            return Optional.empty();
        }
    }


    public void actualizarCita(Long id, Cita cambios) {
        try {
            Cita citaActualizada = citaService.actualizar(id, cambios);
            citaView.mostrarMensaje("Cita actualizada exitosamente");
        } catch (Exception e) {
            citaView.mostrarError("Error al actualizar cita: " + e.getMessage());
        }
    }


    public void confirmarCita(Long id) {
        try {
            citaService.confirmar(id);
            citaView.mostrarMensaje("Cita confirmada exitosamente");
        } catch (Exception e) {
            citaView.mostrarError("Error al confirmar cita: " + e.getMessage());
        }
    }


    public void iniciarCita(Long id) {
        try {
            citaService.iniciar(id);
            citaView.mostrarMensaje("Cita iniciada");
        } catch (Exception e) {
            citaView.mostrarError("Error al iniciar cita: " + e.getMessage());
        }
    }


    public void finalizarCita(Long id) {
        try {
            citaService.finalizar(id);
            citaView.mostrarMensaje("Cita finalizada exitosamente");
        } catch (Exception e) {
            citaView.mostrarError("Error al finalizar cita: " + e.getMessage());
        }
    }


    public void cancelarCita(Long id) {
        try {
            boolean confirmar = citaView.mostrarConfirmacion("¿Está seguro de cancelar esta cita?");
            if (confirmar) {
                citaService.cancelar(id);
                citaView.mostrarMensaje("Cita cancelada exitosamente");
            }
        } catch (Exception e) {
            citaView.mostrarError("Error al cancelar cita: " + e.getMessage());
        }
    }


    public void reprogramarCita(Long id, LocalDateTime nuevoInicio, LocalDateTime nuevoFin, String nuevoMotivo) {
        try {
            Cita citaReprogramada = citaService.reprogramar(id, nuevoInicio, nuevoFin, nuevoMotivo);
            citaView.mostrarMensaje("Cita reprogramada para: " +
                    citaView.formatearFechaHora(nuevoInicio));
        } catch (Exception e) {
            citaView.mostrarError("Error al reprogramar cita: " + e.getMessage());
        }
    }


    public void listarCitasPorFecha(LocalDate fecha) {
        try {
            List<Cita> citas = citaService.listarPorFecha(fecha);
            mostrarListaCitas(citas, "Citas para " + fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {
            citaView.mostrarError("Error al listar citas: " + e.getMessage());
        }
    }


    public void listarCitasPorEstado(CitaEstado estado) {
        try {
            List<Cita> citas = citaService.listarPorEstado(estado);
            mostrarListaCitas(citas, "Citas en estado: " + estado.name());
        } catch (Exception e) {
            citaView.mostrarError("Error al listar citas: " + e.getMessage());
        }
    }


    public void listarCitasVeterinario(Long idVeterinario, LocalDateTime desde, LocalDateTime hasta) {
        try {
            List<Cita> citas = citaService.listarPorVeterinarioYEntre(idVeterinario, desde, hasta);
            mostrarListaCitas(citas, "Citas del veterinario entre " +
                    citaView.formatearFechaHora(desde) + " y " +
                    citaView.formatearFechaHora(hasta));
        } catch (Exception e) {
            citaView.mostrarError("Error al listar citas: " + e.getMessage());
        }
    }


    public void mostrarDetallesCita(Long id) {
        try {
            Optional<Cita> citaOpt = citaService.buscarPorId(id);
            if (citaOpt.isPresent()) {
                Cita cita = citaOpt.get();
                String detalles = String.format("""
                    === DETALLES DE CITA ===
                    ID: %d
                    Veterinario ID: %d
                    Mascota ID: %d
                    Fecha/Hora: %s - %s
                    Estado: %s
                    Motivo: %s
                    """,
                        cita.getId(),
                        cita.getIdVeterinario(),
                        cita.getIdMascota(),
                        citaView.formatearFechaHora(cita.getInicio()),
                        citaView.formatearFechaHora(cita.getFin()),
                        cita.getEstado() != null ? cita.getEstado().name() : "N/A",
                        cita.getMotivo() != null ? cita.getMotivo() : "N/A"
                );
                citaView.mostrarDetallesCita(detalles);
            } else {
                citaView.mostrarError("Cita no encontrada");
            }
        } catch (Exception e) {
            citaView.mostrarError("Error al mostrar detalles: " + e.getMessage());
        }
    }


    public void gestionarCita(Long id) {
        try {
            Optional<Cita> citaOpt = citaService.buscarPorId(id);
            if (citaOpt.isEmpty()) {
                citaView.mostrarError("Cita no encontrada");
                return;
            }

            Cita cita = citaOpt.get();
            boolean continuar = true;

            while (continuar) {
                String opcion = citaView.mostrarOpcionesEstado();

                switch (opcion) {
                    case "Confirmar":
                        confirmarCita(id);
                        break;
                    case "Iniciar":
                        iniciarCita(id);
                        break;
                    case "Finalizar":
                        finalizarCita(id);
                        break;
                    case "Cancelar":
                        cancelarCita(id);
                        break;
                    case "Reprogramar":
                        // Aquí podrías pedir los nuevos datos al usuario
                        citaView.mostrarMensaje("Función de reprogramación - implementar diálogo de entrada");
                        break;
                    case "Volver":
                    default:
                        continuar = false;
                        break;
                }

                // Actualizar estado de la cita después de cada operación
                citaOpt = citaService.buscarPorId(id);
                if (citaOpt.isPresent()) {
                    cita = citaOpt.get();
                }
            }

        } catch (Exception e) {
            citaView.mostrarError("Error en gestión de cita: " + e.getMessage());
        }
    }


    public boolean verificarDisponibilidad(Long idVeterinario, LocalDateTime inicio, LocalDateTime fin) {
        try {
            return !citaService.haySolape(idVeterinario, inicio, fin);
        } catch (Exception e) {
            citaView.mostrarError("Error al verificar disponibilidad: " + e.getMessage());
            return false;
        }
    }


    private void mostrarListaCitas(List<Cita> citas, String titulo) {
        if (citas.isEmpty()) {
            citaView.mostrarMensaje("No hay citas para mostrar");
        } else {
            StringBuilder sb = new StringBuilder("=== " + titulo + " ===\n");
            for (int i = 0; i < citas.size(); i++) {
                Cita c = citas.get(i);
                sb.append(String.format("%d. [%s] %s - Vet: %d - Mascota: %d\n",
                        i + 1,
                        c.getEstado() != null ? c.getEstado().name() : "N/A",
                        citaView.formatearFechaHora(c.getInicio()),
                        c.getIdVeterinario(),
                        c.getIdMascota()));
            }
            citaView.mostrarMensaje(sb.toString());
        }
    }

    // Getters para testing
    public CitaService getCitaService() {
        return citaService;
    }

    public MascotaService getMascotaService() {
        return mascotaService;
    }

    public VeterinarioService getVeterinarioService() {
        return veterinarioService;
    }

    public CitaView getCitaView() {
        return citaView;
    }

    // Métodos añadidos desde versión integrada

    
    
    public void run() {
        System.out.println("=== GESTIÓN DE CITAS ===");
        System.out.println("Funcionalidad disponible:");
        System.out.println("- Agendar nueva cita");
        System.out.println("- Consultar disponibilidad de veterinarios");
        System.out.println("- Confirmar citas pendientes");
        System.out.println("- Iniciar y finalizar consultas");
        System.out.println("- Cancelar y reprogramar citas");
        System.out.println("- Listar citas por fecha y estado");
        System.out.println("- Gestionar agenda de veterinarios");
        System.out.println();
        System.out.println("Esta sección está lista para ser utilizada.");
        System.out.println("Presione ENTER para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }

}
