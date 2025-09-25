package com.happyfeet.service.impl;

import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.enums.CitaEstado;
import com.happyfeet.repository.CitaRepository;
import com.happyfeet.service.CitaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = Objects.requireNonNull(citaRepository, "citaRepository no puede ser null");
    }

    @Override
    public Cita crear(Cita cita) {
        validarNoNull(cita, "cita");
        validarNuevaCita(cita);
        LocalDateTime inicio = cita.getInicio();
        LocalDateTime fin = cita.getFin() != null ? cita.getFin() : inicio.plusMinutes(30);
        if (citaRepository.existsOverlap(cita.getIdVeterinario(), inicio, fin)) {
            throw new ConflictoDeDatosException("El veterinario ya tiene una cita en ese horario");
        }
        if (cita.getEstado() == null) cita.setEstado(CitaEstado.getEstadoInicial());
        return citaRepository.save(cita);
    }

    @Override
    public Cita actualizar(Long id, Cita cambios) {
        validarNoNull(id, "id");
        validarNoNull(cambios, "cambios");
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita con id " + id + " no existe"));
        if (!existente.getEstado().esModificable()) {
            throw new ValidacionException("La cita en estado " + existente.getEstado().getNombre() + " no es modificable");
        }
        Cita merged = mergeCita(existente, cambios);
        validarCitaGenerica(merged);
        LocalDateTime inicio = merged.getInicio();
        LocalDateTime fin = merged.getFin() != null ? merged.getFin() : inicio.plusMinutes(30);
        if (citaRepository.existsOverlap(merged.getIdVeterinario(), inicio, fin)) {
            // Permitir si es la misma cita en el mismo horario (no detectamos por id en query básica)
            // Para simplificar, asumimos que existe solape con otra distinta
            throw new ConflictoDeDatosException("Solape de horarios con otra cita del veterinario");
        }
        return citaRepository.update(merged);
    }

    @Override
    public void confirmar(Long id) { cambiarEstado(id, CitaEstado.CONFIRMADA); }

    @Override
    public void iniciar(Long id) { cambiarEstado(id, CitaEstado.EN_CURSO); }

    @Override
    public void finalizar(Long id) { cambiarEstado(id, CitaEstado.FINALIZADA); }

    @Override
    public void cancelar(Long id) { cambiarEstado(id, CitaEstado.CANCELADA); }

    @Override
    public Cita reprogramar(Long id, LocalDateTime nuevoInicio, LocalDateTime nuevoFin, String nuevoMotivo) {
        validarNoNull(id, "id");
        validarNoNull(nuevoInicio, "nuevoInicio");
        if (nuevoFin != null && nuevoFin.isBefore(nuevoInicio)) {
            throw new ValidacionException("La hora de fin no puede ser anterior al inicio");
        }
        Cita existente = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita con id " + id + " no existe"));
        if (!existente.getEstado().esModificable()) {
            throw new ValidacionException("La cita en estado " + existente.getEstado().getNombre() + " no es reprogramable");
        }
        LocalDateTime fin = nuevoFin != null ? nuevoFin : nuevoInicio.plusMinutes(30);
        if (citaRepository.existsOverlap(existente.getIdVeterinario(), nuevoInicio, fin)) {
            throw new ConflictoDeDatosException("Ya existe una cita en el horario solicitado");
        }
        existente.setInicio(nuevoInicio);
        existente.setFin(nuevoFin);
        if (nuevoMotivo != null && !nuevoMotivo.isBlank()) existente.setMotivo(nuevoMotivo.trim());
        // Mantenemos el estado (PROGRAMADA/CONFIRMADA), no forzamos REPROGRAMADA para no violar transiciones
        return citaRepository.update(existente);
    }

    @Override
    public Optional<Cita> buscarPorId(Long id) {
        validarNoNull(id, "id");
        return citaRepository.findById(id);
    }

    @Override
    public List<Cita> listarPorFecha(LocalDate fecha) {
        validarNoNull(fecha, "fecha");
        return citaRepository.findByDate(fecha);
    }

    @Override
    public List<Cita> listarPorEstado(CitaEstado estado) {
        validarNoNull(estado, "estado");
        return citaRepository.findByEstado(estado);
    }

    @Override
    public List<Cita> listarPorVeterinarioYEntre(Long idVet, LocalDateTime desde, LocalDateTime hasta) {
        validarNoNull(idVet, "idVet");
        validarNoNull(desde, "desde");
        validarNoNull(hasta, "hasta");
        if (hasta.isBefore(desde)) throw new ValidacionException("Rango de fechas inválido");
        return citaRepository.findByVeterinarioAndRange(idVet, desde, hasta);
    }

    @Override
    public boolean haySolape(Long idVet, LocalDateTime inicio, LocalDateTime fin) {
        validarNoNull(idVet, "idVet");
        validarNoNull(inicio, "inicio");
        if (fin == null) fin = inicio.plusMinutes(30);
        if (fin.isBefore(inicio)) throw new ValidacionException("fin antes de inicio");
        return citaRepository.existsOverlap(idVet, inicio, fin);
    }

    // ----------------- Helpers -----------------
    private void cambiarEstado(Long id, CitaEstado nuevo) {
        validarNoNull(id, "id");
        Cita c = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita con id " + id + " no existe"));
        var error = c.getEstado().validarTransicion(nuevo);
        if (error.isPresent()) throw new ValidacionException(error.get());
        c.setEstado(nuevo);
        citaRepository.update(c);
    }

    private static void validarNuevaCita(Cita c) {
        validarCitaGenerica(c);
        if (c.getEstado() != null && c.getEstado() != CitaEstado.PROGRAMADA) {
            throw new ValidacionException("Una nueva cita debe iniciar en estado PROGRAMADA");
        }
    }

    private static void validarCitaGenerica(Cita c) {
        validarNoNull(c.getIdVeterinario(), "idVeterinario");
        validarNoNull(c.getIdMascota(), "idMascota");
        validarNoNull(c.getInicio(), "inicio");
        if (c.getFin() != null && c.getFin().isBefore(c.getInicio())) {
            throw new ValidacionException("La fecha/hora fin no puede ser anterior al inicio");
        }
        if (c.getMotivo() == null || c.getMotivo().isBlank()) {
            throw new ValidacionException("El motivo de la consulta es obligatorio");
        }
    }

    private static Cita mergeCita(Cita base, Cita cambios) {
        if (cambios.getIdVeterinario() != null) base.setIdVeterinario(cambios.getIdVeterinario());
        if (cambios.getIdMascota() != null) base.setIdMascota(cambios.getIdMascota());
        if (cambios.getInicio() != null) base.setInicio(cambios.getInicio());
        if (cambios.getFin() != null) base.setFin(cambios.getFin());
        if (cambios.getMotivo() != null && !cambios.getMotivo().isBlank()) base.setMotivo(cambios.getMotivo().trim());
        // El estado se cambia por métodos dedicados
        return base;
    }

    private static void validarNoNull(Object o, String campo) {
        if (o == null) throw new ValidacionException(campo + " no puede ser null");
    }

    // Excepciones de dominio
    public static class ValidacionException extends RuntimeException { public ValidacionException(String m) { super(m); } }
    public static class RecursoNoEncontradoException extends RuntimeException { public RecursoNoEncontradoException(String m) { super(m); } }
    public static class ConflictoDeDatosException extends RuntimeException { public ConflictoDeDatosException(String m) { super(m); } }
}
