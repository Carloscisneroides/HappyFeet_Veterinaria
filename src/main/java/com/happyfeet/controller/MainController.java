package com.happyfeet.controller;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.model.entities.Mascota;
import com.happyfeet.model.entities.Cita;
import com.happyfeet.model.entities.enums.CitaEstado;
import com.happyfeet.repository.DuenoRepository;
import com.happyfeet.repository.MascotaRepository;
import com.happyfeet.repository.CitaRepository;
import com.happyfeet.repository.impl.DuenoRepositoryImpl;
import com.happyfeet.repository.impl.MascotaRepositoryImpl;
import com.happyfeet.repository.impl.CitaRepositoryImpl;
import com.happyfeet.service.DuenoService;
import com.happyfeet.service.MascotaService;
import com.happyfeet.service.CitaService;
import com.happyfeet.service.impl.DuenoServiceImpl;
import com.happyfeet.service.impl.MascotaServiceImpl;
import com.happyfeet.service.impl.CitaServiceImpl;
import com.happyfeet.view.ConsoleUtils;
import com.happyfeet.view.MenuPrincipal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MainController {

    private final DuenoService duenoService;
    private final MascotaService mascotaService;
    private final CitaService citaService;

    public MainController() {
        // Instanciar repositorios (DuenoRepositoryImpl es abstracto -> anónimo)
        DuenoRepository duenoRepo = new DuenoRepositoryImpl() {};
        MascotaRepository mascotaRepo = new MascotaRepositoryImpl();
        CitaRepository citaRepo = new CitaRepositoryImpl();

        // Instanciar servicios
        this.duenoService = new DuenoServiceImpl(duenoRepo);
        this.mascotaService = new MascotaServiceImpl(mascotaRepo);
        this.citaService = new CitaServiceImpl(citaRepo);
    }

    public void run() {
        new MenuPrincipal(this).iniciar();
    }

    // ======= DUEÑOS =======
    public void listarDuenos() {
        List<Dueno> list = duenoService.listarTodos();
        System.out.println("\n-- Dueños (" + list.size() + ") --");
        for (Dueno d : list) {
            System.out.printf("#%d | %s | doc:%s | tel:%s | email:%s%n",
                    d.getId(),
                    d.getNombreCompleto(),
                    d.getDocumentoIdentidad(),
                    d.getTelefono(),
                    d.getEmail());
        }
    }

    public void buscarDuenoPorTermino() {
        String t = ConsoleUtils.readNonEmpty("Término (nombre, apellido, doc o email)");
        duenoService.buscarPorDueno(t).forEach(d -> {
            System.out.printf("#%d | %s | doc:%s | tel:%s | email:%s%n",
                    d.getId(), d.getNombreCompleto(), d.getDocumentoIdentidad(), d.getTelefono(), d.getEmail());
        });
    }

    public void crearDueno() {
        String nombre = ConsoleUtils.readNonEmpty("Nombre(s)");
        String apellido = ConsoleUtils.readNonEmpty("Apellido(s)");
        String doc = ConsoleUtils.readNonEmpty("Documento identidad");
        String tel = ConsoleUtils.readOptional("Teléfono");
        String email = ConsoleUtils.readOptional("Email");
        String contacto = ConsoleUtils.readOptional("Contacto emergencia");

        Dueno d = new Dueno();
        d.setNombre(nombre);
        d.setApellido(apellido);
        d.setDocumentoIdentidad(doc);
        d.setTelefono(tel);
        d.setEmail(email);
        d.setContactoEmergencia(contacto);

        Dueno creado = duenoService.crearDueno(d);
        System.out.println("Creado: id=" + creado.getId() + " | " + creado.getNombreCompleto());
    }

    public void actualizarDueno() {
        long id = ConsoleUtils.readLong("Id del dueño");
        Optional<Dueno> opt = duenoService.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("No existe el dueño con id " + id);
            return;
        }
        Dueno cambios = new Dueno();
        cambios.setNombre(ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)").isEmpty() ? opt.get().getNombre() : ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)"));
        cambios.setApellido(ConsoleUtils.readOptional("Nuevo apellido (enter para mantener)").isEmpty() ? opt.get().getApellido() : ConsoleUtils.readOptional("Nuevo apellido (enter para mantener)"));
        String nuevoDoc = ConsoleUtils.readOptional("Nuevo documento (enter para mantener)");
        if (!nuevoDoc.isEmpty()) cambios.setDocumentoIdentidad(nuevoDoc);
        String nuevoTel = ConsoleUtils.readOptional("Nuevo teléfono (enter para mantener)");
        if (!nuevoTel.isEmpty()) cambios.setTelefono(nuevoTel);
        String nuevoEmail = ConsoleUtils.readOptional("Nuevo email (enter para mantener)");
        if (!nuevoEmail.isEmpty()) cambios.setEmail(nuevoEmail);
        String nuevoContacto = ConsoleUtils.readOptional("Nuevo contacto emergencia (enter para mantener)");
        if (!nuevoContacto.isEmpty()) cambios.setContactoEmergencia(nuevoContacto);

        Dueno act = duenoService.actualizarDueno(id, cambios);
        System.out.println("Actualizado: " + act.getNombreCompleto());
    }

    public void eliminarDueno() {
        long id = ConsoleUtils.readLong("Id del dueño a eliminar");
        if (!ConsoleUtils.confirm("Confirmas eliminación?")) return;
        duenoService.eliminarDueno(id);
        System.out.println("Eliminado id=" + id);
    }

    // ======= MASCOTAS =======
    public void listarMascotas() {
        List<Mascota> list = mascotaService.listarTodas();
        System.out.println("\n-- Mascotas (" + list.size() + ") --");
        for (Mascota m : list) {
            System.out.printf("#%d | dueño:%d | %s | microchip:%s%n",
                    m.getId(), m.getDuenoId(), m.getNombre(), m.getMicrochip());
        }
    }

    public void buscarMascotasPorDueno() {
        long duenoId = ConsoleUtils.readLong("Id del dueño");
        mascotaService.buscarPorDueno(duenoId).forEach(m ->
                System.out.printf("#%d | %s | microchip:%s%n", m.getId(), m.getNombre(), m.getMicrochip())
        );
    }

    public void buscarMascotasPorNombre() {
        String nombre = ConsoleUtils.readNonEmpty("Nombre (o parte)");
        mascotaService.buscarPorNombre(nombre).forEach(m ->
                System.out.printf("#%d | %s | dueño:%d%n", m.getId(), m.getNombre(), m.getDuenoId())
        );
    }

    public void crearMascota() {
        long duenoId = ConsoleUtils.readLong("Id del dueño (existente)");
        String nombre = ConsoleUtils.readNonEmpty("Nombre de la mascota");
        String microchip = ConsoleUtils.readOptional("Microchip");
        Mascota m = new Mascota.Builder()
                .setDuenoId((int) duenoId)
                .setNombre(nombre)
                .setMicrochip(microchip.isEmpty() ? null : microchip)
                .setSexo(Mascota.Sexo.DESCONOCIDO)
                .build();
        Mascota creada = mascotaService.crearMascota(m);
        System.out.println("Mascota creada id=" + creada.getId());
    }

    public void actualizarMascota() {
        long id = ConsoleUtils.readLong("Id de la mascota");
        Optional<Mascota> opt = mascotaService.buscarPorId(id);
        if (opt.isEmpty()) {
            System.out.println("No existe la mascota " + id);
            return;
        }
        String nuevoNombre = ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)");
        String nuevoMicro = ConsoleUtils.readOptional("Nuevo microchip (enter para mantener)");

        Mascota cambios = new Mascota.Builder()
                .setId((int) id)
                .setDuenoId(opt.get().getDuenoId())
                .setNombre(nuevoNombre.isEmpty() ? opt.get().getNombre() : nuevoNombre)
                .setMicrochip(nuevoMicro.isEmpty() ? opt.get().getMicrochip() : nuevoMicro)
                .setSexo(opt.get().getSexo())
                .build();
        Mascota act = mascotaService.actualizarMascota(id, cambios);
        System.out.println("Actualizada: " + act.getNombre());
    }

    public void eliminarMascota() {
        long id = ConsoleUtils.readLong("Id de la mascota a eliminar");
        if (!ConsoleUtils.confirm("Confirmas eliminación?")) return;
        mascotaService.eliminarMascota(id);
        System.out.println("Eliminada id=" + id);
    }

    // ======= CITAS =======
    public void listarCitasPorFecha() {
        LocalDate f = ConsoleUtils.readDate("Fecha");
        citaService.listarPorFecha(f).forEach(c ->
                System.out.printf("#%d | vet:%d | mascota:%d | %s -> %s | %s | %s%n",
                        c.getId(), c.getIdVeterinario(), c.getIdMascota(),
                        c.getInicio(), c.getFin(), c.getEstado(), c.getMotivo())
        );
    }

    public void listarCitasPorEstado() {
        System.out.println("Estados: 1.PROGRAMADA 2.CONFIRMADA 3.EN_CURSO 4.FINALIZADA 5.CANCELADA");
        int e = ConsoleUtils.readInt("Seleccione estado");
        CitaEstado estado = switch (e) {
            case 2 -> CitaEstado.CONFIRMADA;
            case 3 -> CitaEstado.EN_CURSO;
            case 4 -> CitaEstado.FINALIZADA;
            case 5 -> CitaEstado.CANCELADA;
            default -> CitaEstado.PROGRAMADA;
        };
        citaService.listarPorEstado(estado).forEach(c ->
                System.out.printf("#%d | vet:%d | mascota:%d | %s -> %s | %s | %s%n",
                        c.getId(), c.getIdVeterinario(), c.getIdMascota(),
                        c.getInicio(), c.getFin(), c.getEstado(), c.getMotivo())
        );
    }

    public void agendarCita() {
        long idVet = ConsoleUtils.readLong("Id del veterinario");
        long idMascota = ConsoleUtils.readLong("Id de la mascota");
        LocalDateTime inicio = ConsoleUtils.readDateTime("Inicio");
        String motivo = ConsoleUtils.readNonEmpty("Motivo");

        Cita c = new Cita();
        c.setIdVeterinario(idVet);
        c.setIdMascota(idMascota);
        c.setInicio(inicio);
        c.setMotivo(motivo);

        Cita creada = citaService.agendar(c);
        System.out.println("Cita agendada id=" + creada.getId());
    }

    public void reprogramarCita() {
        long id = ConsoleUtils.readLong("Id de la cita");
        LocalDateTime nuevoInicio = ConsoleUtils.readDateTime("Nuevo inicio");
        LocalDateTime nuevoFin = ConsoleUtils.readDateTime("Nuevo fin");
        String motivo = ConsoleUtils.readOptional("Nuevo motivo (opcional)");
        Cita c = citaService.reprogramar(id, nuevoInicio, nuevoFin, motivo.isEmpty() ? null : motivo);
        System.out.println("Cita reprogramada: id=" + c.getId());
    }

    public void iniciarCita() {
        long id = ConsoleUtils.readLong("Id de la cita");
        citaService.iniciar(id);
        System.out.println("Cita iniciada.");
    }

    public void finalizarCita() {
        long id = ConsoleUtils.readLong("Id de la cita");
        citaService.finalizar(id);
        System.out.println("Cita finalizada.");
    }

    public void cancelarCita() {
        long id = ConsoleUtils.readLong("Id de la cita");
        citaService.cancelar(id);
        System.out.println("Cita cancelada.");
    }

    public void validarSolapeCita() {
        long idVet = ConsoleUtils.readLong("Id veterinario");
        LocalDateTime inicio = ConsoleUtils.readDateTime("Inicio");
        LocalDateTime fin = ConsoleUtils.readDateTime("Fin");
        boolean solapa = citaService.haySolape(idVet, inicio, fin);
        System.out.println(solapa ? "Hay solape" : "No hay solape");
    }

    // ======= Punto de entrada opcional =======
    public static void main(String[] args) {
        new MainController().run();
    }
}
