package com.happyfeet.controller;

import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.Dueno;
import com.happyfeet.service.MascotaService;
import com.happyfeet.service.DuenoService;
import com.happyfeet.view.MascotaView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MascotaController {

    private final MascotaService mascotaService;
    private final DuenoService duenoService;
    private final MascotaView mascotaView;

    public MascotaController(MascotaService mascotaService, DuenoService duenoService, MascotaView mascotaView) {
        this.mascotaService = mascotaService;
        this.duenoService = duenoService;
        this.mascotaView = mascotaView;
    }

    /**
     * Crear nueva mascota
     */
    public void crearMascota(String nombre, Integer duenoId, Integer razaId, LocalDate fechaNacimiento,
                             Mascota.Sexo sexo, String color, String seniasParticulares, Double pesoActual,
                             String microchip, LocalDate fechaImplantacionMicrochip, Boolean agresivo) {
        try {
            // Verificar que el dueño existe
            Optional<Dueno> dueno = duenoService.buscarPorId(duenoId.longValue());
            if (dueno.isEmpty()) {
                mascotaView.mostrarError("No se encontró dueño con ID: " + duenoId);
                return;
            }

            Mascota nuevaMascota = Mascota.Builder.create()
                    .withNombre(nombre)
                    .withDuenoId(duenoId)
                    .withRazaId(razaId)
                    .withFechaNacimiento(fechaNacimiento)
                    .withSexo(sexo)
                    .withColor(color)
                    .withSeniasParticulares(seniasParticulares)
                    .withPesoActual(pesoActual)
                    .withMicrochip(microchip)
                    .withFechaImplantacionMicrochip(fechaImplantacionMicrochip)
                    .withAgresivo(agresivo)
                    .build();

            Mascota mascotaCreada = mascotaService.crearMascota(nuevaMascota);
            mascotaView.mostrarMensaje("Mascota creada exitosamente: " + mascotaCreada.getNombre());

        } catch (Exception e) {
            mascotaView.mostrarError("Error al crear mascota: " + e.getMessage());
        }
    }

    /**
     * Versión simplificada para crear mascota
     */
    public void crearMascotaBasica(String nombre, Integer duenoId, Mascota.Sexo sexo, String color) {
        crearMascota(nombre, duenoId, null, null, sexo, color, null, null, null, null, null);
    }

    /**
     * Buscar mascota por ID
     */
    public Optional<Mascota> buscarPorId(Long id) {
        try {
            Optional<Mascota> mascota = mascotaService.buscarPorId(id);
            if (mascota.isEmpty()) {
                mascotaView.mostrarMensaje("No se encontró mascota con ID: " + id);
            }
            return mascota;
        } catch (Exception e) {
            mascotaView.mostrarError("Error al buscar mascota: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Buscar mascotas por dueño
     */
    public List<Mascota> buscarPorDueno(Long duenoId) {
        try {
            List<Mascota> mascotas = mascotaService.buscarPorDueno(duenoId);
            if (mascotas.isEmpty()) {
                mascotaView.mostrarMensaje("No se encontraron mascotas para el dueño con ID: " + duenoId);
            }
            return mascotas;
        } catch (Exception e) {
            mascotaView.mostrarError("Error al buscar mascotas: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Actualizar mascota
     */
    public void actualizarMascota(Long id, Mascota cambios) {
        try {
            Mascota mascotaActualizada = mascotaService.actualizarMascota(id, cambios);
            mascotaView.mostrarMensaje("Mascota actualizada exitosamente: " + mascotaActualizada.getNombre());
        } catch (Exception e) {
            mascotaView.mostrarError("Error al actualizar mascota: " + e.getMessage());
        }
    }

    /**
     * Eliminar mascota
     */
    public void eliminarMascota(Long id) {
        try {
            Optional<Mascota> mascota = mascotaService.buscarPorId(id);
            if (mascota.isPresent()) {
                boolean confirmar = mascotaView.mostrarConfirmacion(
                        "¿Está seguro de eliminar la mascota: " + mascota.get().getNombre() + "?"
                );
                if (confirmar) {
                    mascotaService.eliminarMascota(id);
                    mascotaView.mostrarMensaje("Mascota eliminada exitosamente");
                }
            } else {
                mascotaView.mostrarError("No se encontró mascota con ID: " + id);
            }
        } catch (Exception e) {
            mascotaView.mostrarError("Error al eliminar mascota: " + e.getMessage());
        }
    }

    /**
     * Listar todas las mascotas
     */
    public void listarTodas() {
        try {
            List<Mascota> mascotas = mascotaService.listarTodas();
            if (mascotas.isEmpty()) {
                mascotaView.mostrarMensaje("No hay mascotas registradas");
            } else {
                StringBuilder sb = new StringBuilder("=== LISTA DE MASCOTAS ===\n");
                for (int i = 0; i < mascotas.size(); i++) {
                    Mascota m = mascotas.get(i);
                    sb.append(String.format("%d. %s - %s - Dueño ID: %d\n",
                            i + 1, m.getNombre(),
                            m.getSexo() != null ? m.getSexo().name() : "N/A",
                            m.getDuenoId()));
                }
                mascotaView.mostrarMensaje(sb.toString());
            }
        } catch (Exception e) {
            mascotaView.mostrarError("Error al listar mascotas: " + e.getMessage());
        }
    }

    /**
     * Mostrar detalles completos de una mascota
     */
    public void mostrarDetallesMascota(Long id) {
        try {
            Optional<Mascota> mascotaOpt = mascotaService.buscarPorId(id);
            if (mascotaOpt.isPresent()) {
                Mascota m = mascotaOpt.get();
                String detalles = String.format("""
                    === DETALLES DE MASCOTA ===
                    Nombre: %s
                    Dueño ID: %d
                    Sexo: %s
                    Color: %s
                    Peso: %.2f kg
                    Microchip: %s
                    Fecha Nacimiento: %s
                    Señas particulares: %s
                    Alergias: %s
                    Agresivo: %s
                    """,
                        m.getNombre(),
                        m.getDuenoId(),
                        m.getSexo() != null ? m.getSexo().name() : "N/A",
                        m.getColor() != null ? m.getColor() : "N/A",
                        m.getPesoActual() != null ? m.getPesoActual() : 0.0,
                        m.getMicrochip() != null ? m.getMicrochip() : "No registrado",
                        m.getFechaNacimiento() != null ? m.getFechaNacimiento() : "N/A",
                        m.getSeniasParticulares() != null ? m.getSeniasParticulares() : "Ninguna",
                        m.getAlergias() != null ? m.getAlergias() : "Ninguna",
                        m.getAgresivo() != null ? (m.getAgresivo() ? "Sí" : "No") : "N/A"
                );
                mascotaView.mostrarMascotaDetalles(detalles);
            } else {
                mascotaView.mostrarError("Mascota no encontrada");
            }
        } catch (Exception e) {
            mascotaView.mostrarError("Error al mostrar detalles: " + e.getMessage());
        }
    }

    /**
     * Buscar mascotas por nombre
     */
    public void buscarPorNombre(String termino) {
        try {
            List<Mascota> resultados = mascotaService.buscarPorNombre(termino);
            if (resultados.isEmpty()) {
                mascotaView.mostrarMensaje("No se encontraron mascotas con: " + termino);
            } else {
                StringBuilder sb = new StringBuilder("=== RESULTADOS DE BÚSQUEDA ===\n");
                for (Mascota m : resultados) {
                    sb.append(String.format("- %s (%s) - Dueño ID: %d\n",
                            m.getNombre(),
                            m.getSexo() != null ? m.getSexo().name() : "N/A",
                            m.getDuenoId()));
                }
                mascotaView.mostrarMensaje(sb.toString());
            }
        } catch (Exception e) {
            mascotaView.mostrarError("Error en la búsqueda: " + e.getMessage());
        }
    }

    /**
     * Verificar si existe mascota con microchip
     */
    public boolean existePorMicrochip(String microchip) {
        try {
            return mascotaService.existePorMicrochip(microchip);
        } catch (Exception e) {
            mascotaView.mostrarError("Error al verificar microchip: " + e.getMessage());
            return false;
        }
    }

    // Getters para testing
    public MascotaService getMascotaService() {
        return mascotaService;
    }

    public DuenoService getDuenoService() {
        return duenoService;
    }

    public MascotaView getMascotaView() {
        return mascotaView;
    }
}