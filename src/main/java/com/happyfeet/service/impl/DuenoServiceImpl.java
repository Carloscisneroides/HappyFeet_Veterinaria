// /src/main/java/com/happyfeet/service/impl/DuenoServiceImpl.java
package com.happyfeet.service.impl;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.repository.DuenoRepository;
import com.happyfeet.service.DuenoService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class DuenoServiceImpl implements DuenoService {

    private final DuenoRepository duenoRepository;

    public DuenoServiceImpl(DuenoRepository duenoRepository) {
        this.duenoRepository = Objects.requireNonNull(duenoRepository, "duenoRepository no puede ser null");
    }

    // Crea y devuelve el dueño creado
    @Override
    public Dueno crear(Dueno dueno) {
        validarNoNull(dueno, "dueno");
        validarNegocio(dueno);
        validarDocumentoUnico(dueno.getDocumentoIdentidad());
        return duenoRepository.save(dueno);
    }

    // Actualiza y devuelve el dueño actualizado
    @Override
    public Dueno actualizar(Long id, Dueno cambios) {
        validarNoNull(id, "id");
        validarNoNull(cambios, "cambios");
        var existente = duenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Dueño con id " + id + " no existe"));

        // Validaciones previas a aplicar cambios
        if (cambios.getDocumentoIdentidad() != null &&
                !cambios.getDocumentoIdentidad().isBlank() &&
                !cambios.getDocumentoIdentidad().equals(existente.getDocumentoIdentidad())) {
            validarDocumentoUnico(cambios.getDocumentoIdentidad());
        }

        // Aplicación de cambios de forma segura
        var actualizado = mergeDueno(existente, cambios);
        validarNegocio(actualizado);

        return duenoRepository.update(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        validarNoNull(id, "id");
        if (!duenoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Dueño con id " + id + " no existe");
        }
        duenoRepository.deleteById(id);
    }

    @Override
    public Optional<Dueno> buscarPorId(Long id) {
        validarNoNull(id, "id");
        return duenoRepository.findById(id);
    }

    @Override
    public List<Dueno> listarTodos() {
        return duenoRepository.findAll().stream()
                .sorted(Comparator.comparing(Dueno::getApellido)
                        .thenComparing(Dueno::getNombre)
                        .thenComparing(Dueno::getId))
                .toList();
    }

    @Override
    public List<Dueno> buscarPorNombre(String termino) {
        var filtro = normalizar(termino);
        Predicate<Dueno> nombreMatch = d -> normalizar(d.getNombre()).contains(filtro);
        Predicate<Dueno> apellidoMatch = d -> normalizar(d.getApellido()).contains(filtro);
        Predicate<Dueno> documentoMatch = d -> normalizar(d.getDocumentoIdentidad()).contains(filtro);

        return duenoRepository.findAll().stream()
                .filter(nombreMatch.or(apellidoMatch).or(documentoMatch))
                .sorted(Comparator.comparing(Dueno::getApellido).thenComparing(Dueno::getNombre))
                .toList();
    }

    @Override
    public boolean existePorDocumento(String documento) {
        validarTextoNoVacio(documento, "documento");
        return duenoRepository.existsByDocumento(documento.trim());
    }

    // ------------------ Helpers de validación y util ------------------

    private void validarNegocio(Dueno d) {
        validarTextoNoVacio(d.getNombre(), "nombre");
        validarTextoNoVacio(d.getApellido(), "apellido");
        validarTextoNoVacio(d.getDocumentoIdentidad(), "documentoIdentidad");
        if (d.getTelefono() != null && d.getTelefono().length() > 20) {
            throw new ValidacionException("telefono excede longitud permitida");
        }
        if (d.getEmail() != null && !d.getEmail().isBlank() && !d.getEmail().contains("@")) {
            throw new ValidacionException("email inválido");
        }
    }

    private void validarDocumentoUnico(String documento) {
        validarTextoNoVacio(documento, "documentoIdentidad");
        if (duenoRepository.existsByDocumento(documento.trim())) {
            throw new ConflictoDeDatosException("Ya existe un dueño con documento " + documento);
        }
    }

    private static Dueno mergeDueno(Dueno base, Dueno cambios) {
        // Estrategia: si el campo en cambios es null o blank, se mantiene el valor base.
        if (cambios.getNombre() != null && !cambios.getNombre().isBlank()) {
            base.setNombre(cambios.getNombre().trim());
        }
        if (cambios.getApellido() != null && !cambios.getApellido().isBlank()) {
            base.setApellido(cambios.getApellido().trim());
        }
        if (cambios.getDocumentoIdentidad() != null && !cambios.getDocumentoIdentidad().isBlank()) {
            base.setDocumentoIdentidad(cambios.getDocumentoIdentidad().trim());
        }
        if (cambios.getTelefono() != null && !cambios.getTelefono().isBlank()) {
            base.setTelefono(cambios.getTelefono().trim());
        }
        if (cambios.getEmail() != null && !cambios.getEmail().isBlank()) {
            base.setEmail(cambios.getEmail().trim());
        }
        if (cambios.getDireccion() != null && !cambios.getDireccion().isBlank()) {
            base.setDireccion(cambios.getDireccion().trim());
        }
        return base;
    }

    private static String normalizar(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }

    private static void validarNoNull(Object o, String nombreCampo) {
        if (o == null) throw new ValidacionException(nombreCampo + " no puede ser null");
    }

    private static void validarTextoNoVacio(String s, String nombreCampo) {
        if (s == null || s.isBlank()) {
            throw new ValidacionException(nombreCampo + " es obligatorio");
        }
    }

    // ------------------ Excepciones de dominio ------------------

    public static class ValidacionException extends RuntimeException {
        public ValidacionException(String msg) { super(msg); }
    }

    public static class RecursoNoEncontradoException extends RuntimeException {
        public RecursoNoEncontradoException(String msg) { super(msg); }
    }

    public static class ConflictoDeDatosException extends RuntimeException {
        public ConflictoDeDatosException(String msg) { super(msg); }
    }
}