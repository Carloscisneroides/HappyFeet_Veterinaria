package com.happyfeet.model.entities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Cita {
    private Integer id;
    private String codigo;
    private Integer mascotaId;
    private Integer servicioId;
    private Integer veterinarioId;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
    private CitaEstado estado;
    private Urgencia urgencia;
    private String notasPrevias;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    // Referencias a entidades relacionadas (composición)
    private Mascota mascota;
    private Servicio servicio;
    private Veterinario veterinario;

    // Historial de cambios de estado
    private final List<CambioEstado> historialEstados;

    // Constantes de negocio
    private static final LocalTime HORA_APERTURA = LocalTime.of(8, 0);
    private static final LocalTime HORA_CIERRE = LocalTime.of(18, 0);
    private static final int DURACION_MINIMA_MINUTOS = 15;
    private static final int DURACION_MAXIMA_MINUTOS = 180;


    public enum CitaEstado {
        PROGRAMADA {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return nuevoEstado == CONFIRMADA ||
                        nuevoEstado == CANCELADA ||
                        nuevoEstado == NO_ASISTIO;
            }

            @Override
            public String getDescripcion() {
                return "Cita programada pendiente de confirmación";
            }
        },

        CONFIRMADA {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return nuevoEstado == EN_CURSO ||
                        nuevoEstado == CANCELADA;
            }

            @Override
            public String getDescripcion() {
                return "Cita confirmada por el cliente";
            }
        },

        EN_CURSO {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return nuevoEstado == FINALIZADA ||
                        nuevoEstado == CANCELADA;
            }

            @Override
            public String getDescripcion() {
                return "Cita en progreso con el veterinario";
            }
        },

        FINALIZADA {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return false; // Estado final
            }

            @Override
            public String getDescripcion() {
                return "Cita completada exitosamente";
            }
        },

        CANCELADA {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return false; // Estado final
            }

            @Override
            public String getDescripcion() {
                return "Cita cancelada por el cliente o veterinaria";
            }
        },

        NO_ASISTIO {
            @Override
            public boolean puedeTransicionarA(CitaEstado nuevoEstado) {
                return false; // Estado final
            }

            @Override
            public String getDescripcion() {
                return "Cliente no se presentó a la cita";
            }
        };


        public abstract boolean puedeTransicionarA(CitaEstado nuevoEstado);


        public abstract String getDescripcion();
    }


    public enum Urgencia {
        BAJA(1, "Baja", "Verde"),
        MEDIA(2, "Media", "Amarillo"),
        ALTA(3, "Alta", "Rojo");

        private final int prioridad;
        private final String descripcion;
        private final String color;

        Urgencia(int prioridad, String descripcion, String color) {
            this.prioridad = prioridad;
            this.descripcion = descripcion;
            this.color = color;
        }

        public int getPrioridad() { return prioridad; }
        public String getDescripcion() { return descripcion; }
        public String getColor() { return color; }

        public boolean esMasUrgenteQue(Urgencia otra) {
            return this.prioridad > otra.prioridad;
        }
    }


    public static class CambioEstado {
        private final CitaEstado estadoAnterior;
        private final CitaEstado estadoNuevo;
        private final LocalDateTime fechaCambio;
        private final String motivo;
        private final String usuario;

        public CambioEstado(CitaEstado estadoAnterior, CitaEstado estadoNuevo,
                            String motivo, String usuario) {
            this.estadoAnterior = estadoAnterior;
            this.estadoNuevo = estadoNuevo;
            this.fechaCambio = LocalDateTime.now();
            this.motivo = motivo;
            this.usuario = usuario;
        }


        public CitaEstado getEstadoAnterior() { return estadoAnterior; }
        public CitaEstado getEstadoNuevo() { return estadoNuevo; }
        public LocalDateTime getFechaCambio() { return fechaCambio; }
        public String getMotivo() { return motivo; }
        public String getUsuario() { return usuario; }

        @Override
        public String toString() {
            return String.format("%s -> %s | %s | Por: %s | Motivo: %s",
                    estadoAnterior, estadoNuevo, fechaCambio, usuario, motivo);
        }
    }

    // Constructor privado para Builder
    private Cita() {
        this.estado = CitaEstado.PROGRAMADA;
        this.urgencia = Urgencia.BAJA;
        this.fechaCreacion = LocalDateTime.now();
        this.historialEstados = new ArrayList<>();
        // Registrar estado inicial en historial
        registrarCambioEstado(null, CitaEstado.PROGRAMADA, "Creación de cita", "Sistema");
    }


    public static class Builder {
        private final Cita cita;

        public Builder() {
            this.cita = new Cita();
        }

        public Builder withMascotaId(Integer mascotaId) {
            cita.mascotaId = Objects.requireNonNull(mascotaId, "ID de mascota es requerido");
            if (mascotaId <= 0) {
                throw new IllegalArgumentException("ID de mascota debe ser positivo");
            }
            return this;
        }

        public Builder withServicioId(Integer servicioId) {
            cita.servicioId = Objects.requireNonNull(servicioId, "ID de servicio es requerido");
            if (servicioId <= 0) {
                throw new IllegalArgumentException("ID de servicio debe ser positivo");
            }
            return this;
        }

        public Builder withVeterinarioId(Integer veterinarioId) {
            cita.veterinarioId = Objects.requireNonNull(veterinarioId, "ID de veterinario es requerido");
            if (veterinarioId <= 0) {
                throw new IllegalArgumentException("ID de veterinario debe ser positivo");
            }
            return this;
        }

        public Builder withFechaHora(LocalDateTime fechaHora) {
            cita.fechaHora = Objects.requireNonNull(fechaHora, "Fecha y hora son requeridas");

            // Validar que no sea en el pasado
            if (fechaHora.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("La cita no puede ser en el pasado");
            }

            // Validar horario laboral
            if (!cita.esHorarioLaboral(fechaHora.toLocalTime())) {
                throw new IllegalArgumentException(
                        "La cita debe estar entre " + HORA_APERTURA + " y " + HORA_CIERRE);
            }

            return this;
        }

        public Builder withMotivoConsulta(String motivoConsulta) {
            cita.motivoConsulta = Objects.requireNonNull(motivoConsulta,
                    "Motivo de consulta es requerido");
            if (motivoConsulta.trim().isEmpty()) {
                throw new IllegalArgumentException("Motivo de consulta no puede estar vacío");
            }
            if (motivoConsulta.length() < 10) {
                throw new IllegalArgumentException("Motivo de consulta muy breve (mínimo 10 caracteres)");
            }
            return this;
        }

        public Builder withUrgencia(Urgencia urgencia) {
            cita.urgencia = urgencia != null ? urgencia : Urgencia.BAJA;
            return this;
        }

        public Builder withNotasPrevias(String notasPrevias) {
            cita.notasPrevias = notasPrevias;
            return this;
        }

        public Builder withMascota(Mascota mascota) {
            cita.mascota = Objects.requireNonNull(mascota, "Mascota no puede ser nula");
            cita.mascotaId = mascota.getId();
            return this;
        }

        public Builder withServicio(Servicio servicio) {
            cita.servicio = Objects.requireNonNull(servicio, "Servicio no puede ser nulo");
            cita.servicioId = servicio.getId();
            return this;
        }

        public Builder withVeterinario(Veterinario veterinario) {
            cita.veterinario = Objects.requireNonNull(veterinario, "Veterinario no puede ser nulo");
            cita.veterinarioId = veterinario.getId();
            return this;
        }

        public Cita build() {
            // Validaciones finales del builder
            if (cita.mascotaId == null) {
                throw new IllegalStateException("ID de mascota es requerido");
            }
            if (cita.servicioId == null) {
                throw new IllegalStateException("ID de servicio es requerido");
            }
            if (cita.veterinarioId == null) {
                throw new IllegalStateException("ID de veterinario es requerido");
            }
            if (cita.fechaHora == null) {
                throw new IllegalStateException("Fecha y hora son requeridas");
            }
            if (cita.motivoConsulta == null) {
                throw new IllegalStateException("Motivo de consulta es requerido");
            }

            // Generar código único
            cita.codigo = generarCodigoCita();

            return cita;
        }

        private String generarCodigoCita() {
            return "CITA-" + System.currentTimeMillis() + "-" +
                    (cita.veterinarioId != null ? cita.veterinarioId : "0");
        }
    }


    public void cambiarEstado(CitaEstado nuevoEstado, String motivo, String usuario) {
        Objects.requireNonNull(nuevoEstado, "Nuevo estado no puede ser nulo");
        Objects.requireNonNull(motivo, "Motivo no puede ser nulo");
        Objects.requireNonNull(usuario, "Usuario no puede ser nulo");

        if (!estado.puedeTransicionarA(nuevoEstado)) {
            throw new IllegalStateException(
                    String.format("Transición inválida: %s -> %s", estado, nuevoEstado));
        }

        CitaEstado estadoAnterior = this.estado;
        this.estado = nuevoEstado;
        this.fechaActualizacion = LocalDateTime.now();

        registrarCambioEstado(estadoAnterior, nuevoEstado, motivo, usuario);
    }


    public void confirmar(String usuario) {
        cambiarEstado(CitaEstado.CONFIRMADA, "Cita confirmada por cliente", usuario);
    }


    public void iniciar(String usuario) {
        if (fechaHora.isAfter(LocalDateTime.now().plusMinutes(30))) {
            throw new IllegalStateException("No se puede iniciar una cita con más de 30 minutos de anticipación");
        }
        cambiarEstado(CitaEstado.EN_CURSO, "Cita iniciada por veterinario", usuario);
    }


    public void finalizar(String usuario) {
        cambiarEstado(CitaEstado.FINALIZADA, "Cita completada exitosamente", usuario);
    }


    public void cancelar(String motivo, String usuario) {
        String motivoCompleto = "Cancelación: " +
                (motivo != null ? motivo : "Sin motivo especificado");
        cambiarEstado(CitaEstado.CANCELADA, motivoCompleto, usuario);
    }


    public void marcarComoNoAsistio(String usuario) {
        if (fechaHora.isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("No se puede marcar como no asistió una cita futura");
        }
        cambiarEstado(CitaEstado.NO_ASISTIO, "Cliente no se presentó a la cita", usuario);
    }


    public boolean puedeSerModificada() {
        return estado == CitaEstado.PROGRAMADA || estado == CitaEstado.CONFIRMADA;
    }


    public boolean estaActiva() {
        return estado != CitaEstado.FINALIZADA &&
                estado != CitaEstado.CANCELADA &&
                estado != CitaEstado.NO_ASISTIO;
    }


    public boolean esParaHoy() {
        return fechaHora != null &&
                fechaHora.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public boolean esUrgente() {
        return urgencia == Urgencia.ALTA;
    }


    public LocalDateTime calcularFinEstimado() {
        if (servicio != null && servicio.getDuracionEstimada() > 0) {
            return fechaHora.plusMinutes(servicio.getDuracionEstimada());
        }
        // Duración por defecto: 30 minutos
        return fechaHora.plusMinutes(30);
    }


    public boolean seSolapaCon(Cita otraCita) {
        if (!Objects.equals(this.veterinarioId, otraCita.veterinarioId)) {
            return false; // Diferentes veterinarios, no hay solapamiento
        }

        LocalDateTime finEsta = this.calcularFinEstimado();
        LocalDateTime finOtra = otraCita.calcularFinEstimado();

        return this.fechaHora.isBefore(finOtra) &&
                otraCita.fechaHora.isBefore(finEsta);
    }


    private boolean esHorarioLaboral(LocalTime hora) {
        return !hora.isBefore(HORA_APERTURA) && !hora.isAfter(HORA_CIERRE);
    }


    private void registrarCambioEstado(CitaEstado estadoAnterior, CitaEstado estadoNuevo,
                                       String motivo, String usuario) {
        CambioEstado cambio = new CambioEstado(estadoAnterior, estadoNuevo, motivo, usuario);
        historialEstados.add(cambio);
    }

    public List<CambioEstado> getHistorialEstados() {
        return List.copyOf(historialEstados);
    }

    public CambioEstado getUltimoCambioEstado() {
        return historialEstados.isEmpty() ? null :
                historialEstados.get(historialEstados.size() - 1);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) {
        if (id != null && id <= 0) throw new IllegalArgumentException("ID debe ser positivo");
        this.id = id;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {
        this.codigo = Objects.requireNonNull(codigo, "Código no puede ser nulo");
    }

    public Integer getMascotaId() { return mascotaId; }
    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = Objects.requireNonNull(mascotaId, "Mascota ID no puede ser nulo");
    }

    public Integer getServicioId() { return servicioId; }
    public void setServicioId(Integer servicioId) {
        this.servicioId = Objects.requireNonNull(servicioId, "Servicio ID no puede ser nulo");
    }

    public Integer getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Integer veterinarioId) {
        this.veterinarioId = Objects.requireNonNull(veterinarioId, "Veterinario ID no puede ser nulo");
    }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = Objects.requireNonNull(fechaHora, "Fecha y hora son requeridas");
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La cita no puede ser en el pasado");
        }
    }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = Objects.requireNonNull(motivoConsulta, "Motivo es requerido");
    }

    public CitaEstado getEstado() { return estado; }
    // No hay setter público para estado - usar cambiarEstado()

    public Urgencia getUrgencia() { return urgencia; }
    public void setUrgencia(Urgencia urgencia) {
        this.urgencia = urgencia != null ? urgencia : Urgencia.BAJA;
    }

    public String getNotasPrevias() { return notasPrevias; }
    public void setNotasPrevias(String notasPrevias) { this.notasPrevias = notasPrevias; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) {
        this.mascota = Objects.requireNonNull(mascota, "Mascota no puede ser nula");
        this.mascotaId = mascota.getId();
    }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) {
        this.servicio = Objects.requireNonNull(servicio, "Servicio no puede ser nulo");
        this.servicioId = servicio.getId();
    }

    public Veterinario getVeterinario() { return veterinario; }
    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = Objects.requireNonNull(veterinario, "Veterinario no puede ser nulo");
        this.veterinarioId = veterinario.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cita otraCita = (Cita) obj;
        return Objects.equals(codigo, otraCita.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("""
            📅 CITA #%s
            Estado: %s (%s)
            Fecha: %s
            Mascota: %s
            Veterinario: %s
            Servicio: %s
            Urgencia: %s
            Motivo: %s
            """,
                codigo,
                estado,
                estado.getDescripcion(),
                fechaHora != null ? fechaHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A",
                mascota != null ? mascota.getNombre() : "Mascota ID: " + mascotaId,
                veterinario != null ? veterinario.getNombreCompleto() : "Veterinario ID: " + veterinarioId,
                servicio != null ? servicio.getNombre() : "Servicio ID: " + servicioId,
                urgencia.getDescripcion(),
                motivoConsulta.length() > 50 ? motivoConsulta.substring(0, 50) + "..." : motivoConsulta
        );
    }


    /**
     * Factory method para crear cita de urgencia.
     */
    public static Cita crearCitaUrgencia(Mascota mascota, Veterinario veterinario,
                                         Servicio servicio, String motivo) {
        return new Builder()
                .withMascota(mascota)
                .withVeterinario(veterinario)
                .withServicio(servicio)
                .withFechaHora(LocalDateTime.now().plusMinutes(15)) // Próximos 15 minutos
                .withMotivoConsulta("URGENCIA: " + motivo)
                .withUrgencia(Urgencia.ALTA)
                .build();
    }


    public static Cita crearCitaControl(Mascota mascota, Veterinario veterinario,
                                        LocalDateTime fecha, String observaciones) {
        return new Builder()
                .withMascota(mascota)
                .withVeterinario(veterinario)
                .withFechaHora(fecha)
                .withMotivoConsulta("Control rutinario: " + observaciones)
                .withUrgencia(Urgencia.BAJA)
                .build();
    }
}