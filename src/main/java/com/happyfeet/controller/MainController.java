package com.happyfeet.controller;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.enums.CitaEstado;
import com.happyfeet.repository.*;
import com.happyfeet.repository.impl.*;
import com.happyfeet.repository.impl.InventarioRepositoryImpl;
import com.happyfeet.service.*;
import com.happyfeet.service.impl.*;
import com.happyfeet.view.ConsoleUtils;
import com.happyfeet.view.MenuPrincipal;
import com.happyfeet.util.FileLogger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal del Sistema Happy Feet Veterinaria.
 * Implementa todos los 5 módulos requeridos según especificaciones.
 */
public class MainController {

    private static final FileLogger LOG = FileLogger.getInstance();

    // ============ SERVICIOS Y REPOSITORIOS ============
    private final DuenoService duenoService;
    private final MascotaService mascotaService;
    private final CitaService citaService;
    private final InventarioService inventarioService;
    private final FacturaService facturaService;

    // ============ CONTROLADORES ESPECIALIZADOS ============
    private final InventarioController inventarioController;
    private final FacturaController facturaController;
    private final ActividadesEspecialesController actividadesController;

    public MainController() {
        LOG.info("Inicializando MainController y todos los servicios del sistema");

        try {
            // Instanciar repositorios
            DuenoRepository duenoRepo = new DuenoRepositoryImpl();
            MascotaRepository mascotaRepo = new MascotaRepositoryImpl();
            CitaRepository citaRepo = new CitaRepositoryImpl();
            InventarioRepository inventarioRepo = new InventarioRepositoryImpl();
            FacturaRepository facturaRepo = new FacturaRepositoryImpl();

            // Instanciar servicios
            this.duenoService = new DuenoServiceImpl(duenoRepo);
            this.mascotaService = new MascotaServiceImpl(mascotaRepo);
            this.citaService = new CitaServiceImpl(citaRepo);
            this.inventarioService = new InventarioService(inventarioRepo);
            this.facturaService = new FacturaServiceImpl(facturaRepo);

            // Instanciar controladores especializados
            this.inventarioController = new InventarioController(inventarioService, inventarioRepo);
            this.facturaController = new FacturaController(facturaService, duenoService, inventarioService, inventarioRepo);
            this.actividadesController = new ActividadesEspecialesController(mascotaService, duenoService);

            LOG.info("Todos los módulos del sistema inicializados correctamente");

        } catch (Exception e) {
            LOG.error("Error crítico inicializando el sistema", e);
            throw new RuntimeException("No se pudo inicializar el sistema", e);
        }
    }

    public void run() {
        LOG.info("Iniciando sistema Happy Feet Veterinaria");
        mostrarBienvenida();
        new MenuPrincipalCompleto(this).iniciar();
        LOG.info("Sistema Happy Feet finalizado");
    }

    private void mostrarBienvenida() {
        System.out.println("""
            
            ████████████████████████████████████████████████
            █                                              █
            █           🐾 HAPPY FEET VETERINARIA 🐾       █
            █                                              █
            █          Sistema de Gestión Integral         █
            █                 Versión 1.0                  █
            █                                              █
            ████████████████████████████████████████████████
            
            """);
    }

    // ============ MÓDULO 1: GESTIÓN DE PACIENTES (DUEÑOS Y MASCOTAS) ============

    public void listarDuenos() {
        List<Dueno> list = duenoService.listarTodos();
        System.out.println("\n=== DUEÑOS REGISTRADOS (" + list.size() + ") ===");
        for (Dueno d : list) {
            System.out.printf("#%d | %s | Doc: %s | Tel: %s | Email: %s%n",
                    d.getId(),
                    d.getNombreCompleto(),
                    d.getDocumentoIdentidad(),
                    d.getTelefono() != null ? d.getTelefono() : "N/A",
                    d.getEmail());
        }
    }

    public void buscarDuenoPorTermino() {
        String termino = ConsoleUtils.readNonEmpty("Término de búsqueda (nombre, doc, email)");
        List<Dueno> resultados = duenoService.buscarPorDueno(termino);

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron dueños con: " + termino);
            return;
        }

        System.out.println("\n=== RESULTADOS DE BÚSQUEDA ===");
        resultados.forEach(d -> {
            System.out.printf("#%d | %s | Doc: %s | Tel: %s | Email: %s%n",
                    d.getId(), d.getNombreCompleto(), d.getDocumentoIdentidad(),
                    d.getTelefono(), d.getEmail());
        });
    }

    public void crearDueno() {
        try {
            String nombre = ConsoleUtils.readNonEmpty("Nombre completo");
            String doc = ConsoleUtils.readNonEmpty("Documento de identidad");
            String tel = ConsoleUtils.readOptional("Teléfono");
            String email = ConsoleUtils.readNonEmpty("Email");
            String direccion = ConsoleUtils.readOptional("Dirección");
            String contactoEmergencia = ConsoleUtils.readOptional("Contacto de emergencia");

            Dueno nuevoDueno = Dueno.Builder.create()
                    .withNombreCompleto(nombre)
                    .withDocumentoIdentidad(doc)
                    .withTelefono(tel.isEmpty() ? null : tel)
                    .withEmail(email)
                    .withDireccion(direccion.isEmpty() ? null : direccion)
                    .withContactoEmergencia(contactoEmergencia.isEmpty() ? null : contactoEmergencia)
                    .build();

            Dueno creado = duenoService.crearDueno(nuevoDueno);
            LOG.info("Dueño creado: " + creado.getNombreCompleto() + " (ID: " + creado.getId() + ")");
            System.out.println("✅ Dueño creado exitosamente: " + creado.getNombreCompleto());

        } catch (Exception e) {
            LOG.error("Error creando dueño", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void actualizarDueno() {
        try {
            long id = ConsoleUtils.readLong("ID del dueño a actualizar");
            Optional<Dueno> opt = duenoService.buscarPorId(id);

            if (opt.isEmpty()) {
                System.out.println("No existe dueño con ID " + id);
                return;
            }

            Dueno base = opt.get();
            System.out.println("Dueño actual: " + base.getNombreCompleto());

            String nuevoNombre = ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)");
            String nuevoDoc = ConsoleUtils.readOptional("Nuevo documento (enter para mantener)");
            String nuevoTel = ConsoleUtils.readOptional("Nuevo teléfono (enter para mantener)");
            String nuevoEmail = ConsoleUtils.readOptional("Nuevo email (enter para mantener)");

            Dueno cambios = Dueno.Builder.create()
                    .withId(base.getId())
                    .withNombreCompleto(nuevoNombre.isEmpty() ? base.getNombreCompleto() : nuevoNombre)
                    .withDocumentoIdentidad(nuevoDoc.isEmpty() ? base.getDocumentoIdentidad() : nuevoDoc)
                    .withTelefono(nuevoTel.isEmpty() ? base.getTelefono() : nuevoTel)
                    .withEmail(nuevoEmail.isEmpty() ? base.getEmail() : nuevoEmail)
                    .withDireccion(base.getDireccion())
                    .withContactoEmergencia(base.getContactoEmergencia())
                    .build();

            Dueno actualizado = duenoService.actualizarDueno(id, cambios);
            System.out.println("✅ Dueño actualizado: " + actualizado.getNombreCompleto());

        } catch (Exception e) {
            LOG.error("Error actualizando dueño", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void eliminarDueno() {
        try {
            long id = ConsoleUtils.readLong("ID del dueño a eliminar");

            if (!ConsoleUtils.confirm("¿Confirma la eliminación?")) {
                System.out.println("Eliminación cancelada");
                return;
            }

            duenoService.eliminarDueno(id);
            LOG.info("Dueño eliminado ID: " + id);
            System.out.println("✅ Dueño eliminado exitosamente");

        } catch (Exception e) {
            LOG.error("Error eliminando dueño", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ---- MASCOTAS ----

    public void listarMascotas() {
        List<Mascota> list = mascotaService.listarTodas();
        System.out.println("\n=== MASCOTAS REGISTRADAS (" + list.size() + ") ===");
        for (Mascota m : list) {
            System.out.printf("#%d | %s | Dueño: %d | Microchip: %s | Sexo: %s%n",
                    m.getId(), m.getNombre(), m.getDuenoId(),
                    m.getMicrochip() != null ? m.getMicrochip() : "N/A",
                    m.getSexo() != null ? m.getSexo() : "N/A");
        }
    }

    public void buscarMascotasPorDueno() {
        long duenoId = ConsoleUtils.readLong("ID del dueño");
        List<Mascota> mascotas = mascotaService.buscarPorDueno(duenoId);

        if (mascotas.isEmpty()) {
            System.out.println("No se encontraron mascotas para el dueño ID: " + duenoId);
            return;
        }

        System.out.println("\n=== MASCOTAS DEL DUEÑO ===");
        mascotas.forEach(m ->
                System.out.printf("#%d | %s | Microchip: %s%n",
                        m.getId(), m.getNombre(), m.getMicrochip())
        );
    }

    public void buscarMascotasPorNombre() {
        String nombre = ConsoleUtils.readNonEmpty("Nombre de la mascota");
        List<Mascota> mascotas = mascotaService.buscarPorNombre(nombre);

        if (mascotas.isEmpty()) {
            System.out.println("No se encontraron mascotas con nombre: " + nombre);
            return;
        }

        System.out.println("\n=== RESULTADOS ===");
        mascotas.forEach(m ->
                System.out.printf("#%d | %s | Dueño: %d%n",
                        m.getId(), m.getNombre(), m.getDuenoId())
        );
    }

    public void crearMascota() {
        try {
            long duenoId = ConsoleUtils.readLong("ID del dueño");

            // Verificar que el dueño existe
            if (duenoService.buscarPorId(duenoId).isEmpty()) {
                System.out.println("No existe dueño con ID: " + duenoId);
                return;
            }

            String nombre = ConsoleUtils.readNonEmpty("Nombre de la mascota");
            String microchip = ConsoleUtils.readOptional("Microchip (opcional)");

            System.out.println("Sexo: 1-Macho, 2-Hembra");
            int sexoOpcion = ConsoleUtils.readInt("Seleccione sexo");
            Mascota.Sexo sexo = sexoOpcion == 1 ? Mascota.Sexo.MACHO : Mascota.Sexo.HEMBRA;

            Mascota nuevaMascota = Mascota.Builder.create()
                    .withDuenoId((int) duenoId)
                    .withNombre(nombre)
                    .withRazaId(1) // Por defecto
                    .withMicrochip(microchip.isEmpty() ? null : microchip)
                    .withSexo(sexo)
                    .build();

            Mascota creada = mascotaService.crearMascota(nuevaMascota);
            LOG.info("Mascota creada: " + creada.getNombre() + " (ID: " + creada.getId() + ")");
            System.out.println("✅ Mascota creada exitosamente: " + creada.getNombre());

        } catch (Exception e) {
            LOG.error("Error creando mascota", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void actualizarMascota() {
        try {
            long id = ConsoleUtils.readLong("ID de la mascota");
            Optional<Mascota> opt = mascotaService.buscarPorId(id);

            if (opt.isEmpty()) {
                System.out.println("No existe mascota con ID " + id);
                return;
            }

            Mascota base = opt.get();
            System.out.println("Mascota actual: " + base.getNombre());

            String nuevoNombre = ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)");
            String nuevoMicrochip = ConsoleUtils.readOptional("Nuevo microchip (enter para mantener)");

            Mascota cambios = Mascota.Builder.create()
                    .withId((int) id)
                    .withDuenoId(base.getDuenoId())
                    .withNombre(nuevoNombre.isEmpty() ? base.getNombre() : nuevoNombre)
                    .withRazaId(base.getRazaId() != null ? base.getRazaId() : 1)
                    .withMicrochip(nuevoMicrochip.isEmpty() ? base.getMicrochip() : nuevoMicrochip)
                    .withSexo(base.getSexo() != null ? base.getSexo() : Mascota.Sexo.MACHO)
                    .build();

            Mascota actualizada = mascotaService.actualizarMascota(id, cambios);
            System.out.println("✅ Mascota actualizada: " + actualizada.getNombre());

        } catch (Exception e) {
            LOG.error("Error actualizando mascota", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void eliminarMascota() {
        try {
            long id = ConsoleUtils.readLong("ID de la mascota a eliminar");

            if (!ConsoleUtils.confirm("¿Confirma la eliminación?")) {
                System.out.println("Eliminación cancelada");
                return;
            }

            mascotaService.eliminarMascota(id);
            LOG.info("Mascota eliminada ID: " + id);
            System.out.println("✅ Mascota eliminada exitosamente");

        } catch (Exception e) {
            LOG.error("Error eliminando mascota", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ============ MÓDULO 2: SERVICIOS MÉDICOS Y CITAS ============

    public void listarCitasPorFecha() {
        LocalDate fecha = ConsoleUtils.readDate("Fecha de las citas");
        List<Cita> citas = citaService.listarPorFecha(fecha);

        if (citas.isEmpty()) {
            System.out.println("No hay citas para la fecha: " + fecha);
            return;
        }

        System.out.println("\n=== CITAS DEL " + fecha + " ===");
        citas.forEach(c ->
                System.out.printf("#%d | Vet: %d | Mascota: %d | %s | %s | %s%n",
                        c.getId(), c.getIdVeterinario(), c.getIdMascota(),
                        c.getInicio(), c.getEstado().getNombre(),
                        c.getMotivo() != null ? c.getMotivo() : "N/A")
        );
    }

    public void listarCitasPorEstado() {
        System.out.println("Estados disponibles:");
        System.out.println("1. PROGRAMADA");
        System.out.println("2. CONFIRMADA");
        System.out.println("3. EN_CURSO");
        System.out.println("4. FINALIZADA");
        System.out.println("5. CANCELADA");

        int opcion = ConsoleUtils.readInt("Seleccione estado");
        CitaEstado estado = switch (opcion) {
            case 2 -> CitaEstado.CONFIRMADA;
            case 3 -> CitaEstado.EN_CURSO;
            case 4 -> CitaEstado.FINALIZADA;
            case 5 -> CitaEstado.CANCELADA;
            default -> CitaEstado.PROGRAMADA;
        };

        List<Cita> citas = citaService.listarPorEstado(estado);

        if (citas.isEmpty()) {
            System.out.println("No hay citas en estado: " + estado.getNombre());
            return;
        }

        System.out.println("\n=== CITAS " + estado.getNombre().toUpperCase() + " ===");
        citas.forEach(c ->
                System.out.printf("#%d | Vet: %d | Mascota: %d | %s | %s%n",
                        c.getId(), c.getIdVeterinario(), c.getIdMascota(),
                        c.getInicio(), c.getMotivo())
        );
    }

    public void agendarCita() {
        try {
            long idVet = ConsoleUtils.readLong("ID del veterinario");
            long idMascota = ConsoleUtils.readLong("ID de la mascota");
            LocalDateTime inicio = ConsoleUtils.readDateTime("Fecha y hora de inicio");
            String motivo = ConsoleUtils.readNonEmpty("Motivo de la consulta");

            // Verificar disponibilidad
            if (citaService.haySolape(idVet, inicio, inicio.plusHours(1))) {
                System.out.println("❌ El veterinario ya tiene una cita en ese horario");
                return;
            }

            Cita nuevaCita = new Cita();
            nuevaCita.setIdVeterinario(idVet);
            nuevaCita.setIdMascota(idMascota);
            nuevaCita.setInicio(inicio);
            nuevaCita.setMotivo(motivo);

            Cita creada = citaService.crear(nuevaCita);
            LOG.info("Cita agendada: ID " + creada.getId() + " para " + inicio);
            System.out.println("✅ Cita agendada exitosamente (ID: " + creada.getId() + ")");

        } catch (Exception e) {
            LOG.error("Error agendando cita", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void reprogramarCita() {
        try {
            long id = ConsoleUtils.readLong("ID de la cita");
            LocalDateTime nuevoInicio = ConsoleUtils.readDateTime("Nueva fecha y hora");
            LocalDateTime nuevoFin = nuevoInicio.plusHours(1);
            String nuevoMotivo = ConsoleUtils.readOptional("Nuevo motivo (opcional)");

            Cita reprogramada = citaService.reprogramar(id, nuevoInicio, nuevoFin,
                    nuevoMotivo.isEmpty() ? null : nuevoMotivo);

            LOG.info("Cita reprogramada: ID " + id + " a " + nuevoInicio);
            System.out.println("✅ Cita reprogramada exitosamente");

        } catch (Exception e) {
            LOG.error("Error reprogramando cita", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void iniciarCita() {
        try {
            long id = ConsoleUtils.readLong("ID de la cita");
            citaService.iniciar(id);
            LOG.info("Cita iniciada: ID " + id);
            System.out.println("✅ Cita iniciada");
        } catch (Exception e) {
            LOG.error("Error iniciando cita", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void finalizarCita() {
        try {
            long id = ConsoleUtils.readLong("ID de la cita");
            citaService.finalizar(id);
            LOG.info("Cita finalizada: ID " + id);
            System.out.println("✅ Cita finalizada");
        } catch (Exception e) {
            LOG.error("Error finalizando cita", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void cancelarCita() {
        try {
            long id = ConsoleUtils.readLong("ID de la cita");

            if (!ConsoleUtils.confirm("¿Confirma la cancelación?")) {
                System.out.println("Cancelación abortada");
                return;
            }

            citaService.cancelar(id);
            LOG.info("Cita cancelada: ID " + id);
            System.out.println("✅ Cita cancelada");
        } catch (Exception e) {
            LOG.error("Error cancelando cita", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void validarSolapeCita() {
        try {
            long idVet = ConsoleUtils.readLong("ID del veterinario");
            LocalDateTime inicio = ConsoleUtils.readDateTime("Fecha y hora de inicio");
            LocalDateTime fin = ConsoleUtils.readDateTime("Fecha y hora de fin");

            boolean haySolape = citaService.haySolape(idVet, inicio, fin);
            System.out.println(haySolape ? "❌ HAY SOLAPE DE HORARIOS" : "✅ No hay solape");

        } catch (Exception e) {
            LOG.error("Error validando solape", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ============ MÓDULO 3: INVENTARIO Y FARMACIA ============

    public void gestionarInventario() {
        while (true) {
            int opcion = ConsoleUtils.menu("Gestión de Inventario y Farmacia",
                    "Listar todos los productos",
                    "Buscar producto por código",
                    "Buscar productos por nombre",
                    "Crear nuevo producto",
                    "Actualizar producto",
                    "Eliminar producto",
                    "Ajustar stock",
                    "Descontar stock",
                    "Productos con stock bajo",
                    "Productos vencidos",
                    "Productos por vencer",
                    "Reporte completo"
            );

            switch (opcion) {
                case 1 -> inventarioController.listarTodosLosProductos();
                case 2 -> inventarioController.buscarProductoPorCodigo();
                case 3 -> inventarioController.buscarProductosPorNombre();
                case 4 -> inventarioController.crearProducto();
                case 5 -> inventarioController.actualizarProducto();
                case 6 -> inventarioController.eliminarProducto();
                case 7 -> inventarioController.ajustarStock();
                case 8 -> inventarioController.descontarStock();
                case 9 -> inventarioController.mostrarProductosConStockBajo();
                case 10 -> inventarioController.mostrarProductosVencidos();
                case 11 -> inventarioController.mostrarProductosPorVencer();
                case 12 -> inventarioController.generarReporteCompleto();
                case 0 -> { return; }
            }
            ConsoleUtils.pause();
        }
    }

    // ============ MÓDULO 4: FACTURACIÓN Y REPORTES ============

    public void gestionarFacturacion() {
        while (true) {
            int opcion = ConsoleUtils.menu("Facturación y Reportes",
                    "Crear nueva factura",
                    "Listar todas las facturas",
                    "Buscar factura",
                    "Pagar factura",
                    "Cancelar factura",
                    "Reporte de ventas",
                    "Facturas vencidas"
            );

            switch (opcion) {
                case 1 -> facturaController.crearFactura();
                case 2 -> facturaController.listarFacturas();
                case 3 -> facturaController.buscarFactura();
                case 4 -> facturaController.pagarFactura();
                case 5 -> facturaController.cancelarFactura();
                case 6 -> facturaController.generarReporteVentas();
                case 7 -> facturaController.mostrarFacturasVencidas();
                case 0 -> { return; }
            }
            ConsoleUtils.pause();
        }
    }

    // ============ MÓDULO 5: ACTIVIDADES ESPECIALES ============

    public void gestionarActividadesEspeciales() {
        while (true) {
            int opcion = ConsoleUtils.menu("Actividades Especiales",
                    "Días de Adopción",
                    "Jornadas de Vacunación",
                    "Club de Mascotas Frecuentes"
            );

            switch (opcion) {
                case 1 -> actividadesController.gestionarAdopciones();
                case 2 -> actividadesController.gestionarJornadasVacunacion();
                case 3 -> actividadesController.gestionarClubFrecuentes();
                case 0 -> { return; }
            }
            ConsoleUtils.pause();
        }
    }

    // ============ MENU PRINCIPAL COMPLETO ============

    /**
     * Clase interna para el menú principal que incluye TODOS los 5 módulos requeridos
     */
    public static class MenuPrincipalCompleto {

        private final MainController controller;

        public MenuPrincipalCompleto(MainController controller) {
            this.controller = controller;
        }

        public void iniciar() {
            while (true) {
                int opcion = ConsoleUtils.menu("HappyFeet Veterinaria - Menú Principal",
                        "Gestión de Pacientes (Dueños y Mascotas)",
                        "Servicios Médicos y Citas",
                        "Inventario y Farmacia",
                        "Facturación y Reportes",
                        "Actividades Especiales"
                );

                switch (opcion) {
                    case 1 -> menuPacientes();
                    case 2 -> menuCitas();
                    case 3 -> controller.gestionarInventario();
                    case 4 -> controller.gestionarFacturacion();
                    case 5 -> controller.gestionarActividadesEspeciales();
                    case 0 -> {
                        System.out.println("¡Gracias por usar Happy Feet Veterinaria!");
                        return;
                    }
                }
            }
        }

        private void menuPacientes() {
            while (true) {
                int opcion = ConsoleUtils.menu("Gestión de Pacientes",
                        "=== DUEÑOS ===",
                        "Listar todos los dueños",
                        "Buscar dueño por término",
                        "Crear nuevo dueño",
                        "Actualizar dueño",
                        "Eliminar dueño",
                        "=== MASCOTAS ===",
                        "Listar todas las mascotas",
                        "Buscar mascotas por dueño",
                        "Buscar mascotas por nombre",
                        "Crear nueva mascota",
                        "Actualizar mascota",
                        "Eliminar mascota"
                );

                switch (opcion) {
                    case 1 -> {} // Separador
                    case 2 -> controller.listarDuenos();
                    case 3 -> controller.buscarDuenoPorTermino();
                    case 4 -> controller.crearDueno();
                    case 5 -> controller.actualizarDueno();
                    case 6 -> controller.eliminarDueno();
                    case 7 -> {} // Separador
                    case 8 -> controller.listarMascotas();
                    case 9 -> controller.buscarMascotasPorDueno();
                    case 10 -> controller.buscarMascotasPorNombre();
                    case 11 -> controller.crearMascota();
                    case 12 -> controller.actualizarMascota();
                    case 13 -> controller.eliminarMascota();
                    case 0 -> { return; }
                }
                ConsoleUtils.pause();
            }
        }

        private void menuCitas() {
            while (true) {
                int opcion = ConsoleUtils.menu("Servicios Médicos y Citas",
                        "Listar citas por fecha",
                        "Listar citas por estado",
                        "Agendar nueva cita",
                        "Reprogramar cita",
                        "Iniciar cita",
                        "Finalizar cita",
                        "Cancelar cita",
                        "Verificar disponibilidad"
                );

                switch (opcion) {
                    case 1 -> controller.listarCitasPorFecha();
                    case 2 -> controller.listarCitasPorEstado();
                    case 3 -> controller.agendarCita();
                    case 4 -> controller.reprogramarCita();
                    case 5 -> controller.iniciarCita();
                    case 6 -> controller.finalizarCita();
                    case 7 -> controller.cancelarCita();
                    case 8 -> controller.validarSolapeCita();
                    case 0 -> { return; }
                }
                ConsoleUtils.pause();
            }
        }
    }

    // ============ GETTERS PARA TESTING ============

    public DuenoService getDuenoService() {
        return duenoService;
    }

    public MascotaService getMascotaService() {
        return mascotaService;
    }

    public CitaService getCitaService() {
        return citaService;
    }

    public InventarioService getInventarioService() {
        return inventarioService;
    }

    public FacturaService getFacturaService() {
        return facturaService;
    }

    public InventarioController getInventarioController() {
        return inventarioController;
    }

    public FacturaController getFacturaController() {
        return facturaController;
    }

    public ActividadesEspecialesController getActividadesController() {
        return actividadesController;
    }

    // ============ PUNTO DE ENTRADA PRINCIPAL ============

    /**
     * Punto de entrada principal del sistema Happy Feet Veterinaria.
     * Inicializa todos los módulos y ejecuta el menú principal.
     */
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema Happy Feet Veterinaria...");

        try {
            MainController controller = new MainController();
            controller.run();
        } catch (Exception e) {
            System.err.println("Error crítico en el sistema: " + e.getMessage());
            FileLogger.getInstance().error("Error crítico en main", e);
            System.exit(1);
        }
    }
}