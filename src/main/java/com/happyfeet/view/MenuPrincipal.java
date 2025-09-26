package com.happyfeet.view;

import com.happyfeet.controller.MainController;

public class MenuPrincipal {

    private final MainController controller;

    public MenuPrincipal(MainController controller) {
        this.controller = controller;
    }

    public void iniciar() {
        while (true) {
            int op = ConsoleUtils.menu("HappyFeet Veterinaria - Menú Principal",
                    "Dueños",
                    "Mascotas",
                    "Citas"
            );
            switch (op) {
                case 1 -> menuDuenos();
                case 2 -> menuMascotas();
                case 3 -> menuCitas();
                case 0 -> {
                    System.out.println("¡Hasta pronto!");
                    return;
                }
            }
        }
    }

    private void menuDuenos() {
        while (true) {
            int op = ConsoleUtils.menu("Gestión de Dueños",
                    "Listar todos",
                    "Buscar por término",
                    "Crear",
                    "Actualizar",
                    "Eliminar"
            );
            switch (op) {
                case 1 -> controller.listarDuenos();
                case 2 -> controller.buscarDuenoPorTermino();
                case 3 -> controller.crearDueno();
                case 4 -> controller.actualizarDueno();
                case 5 -> controller.eliminarDueno();
                case 0 -> { return; }
            }
            ConsoleUtils.pause();
        }
    }

    private void menuMascotas() {
        while (true) {
            int op = ConsoleUtils.menu("Gestión de Mascotas",
                    "Listar todas",
                    "Buscar por dueño",
                    "Buscar por nombre",
                    "Crear",
                    "Actualizar",
                    "Eliminar"
            );
            switch (op) {
                case 1 -> controller.listarMascotas();
                case 2 -> controller.buscarMascotasPorDueno();
                case 3 -> controller.buscarMascotasPorNombre();
                case 4 -> controller.crearMascota();
                case 5 -> controller.actualizarMascota();
                case 6 -> controller.eliminarMascota();
                case 0 -> { return; }
            }
            ConsoleUtils.pause();
        }
    }

    private void menuCitas() {
        while (true) {
            int op = ConsoleUtils.menu("Gestión de Citas",
                    "Listar por fecha",
                    "Listar por estado",
                    "Agendar",
                    "Reprogramar",
                    "Iniciar",
                    "Finalizar",
                    "Cancelar",
                    "Consultar solape"
            );
            switch (op) {
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
