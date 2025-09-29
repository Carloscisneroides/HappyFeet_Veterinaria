package com.happyfeet.view;

import com.happyfeet.config.SimpleDependencyFactory;
import com.happyfeet.controller.*;
import com.happyfeet.util.AppLogger;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipalDemo {

    private final Scanner scanner;
    private final SimpleDependencyFactory factory;
    private boolean running;

    public MenuPrincipalDemo() {
        this.scanner = new Scanner(System.in);
        this.factory = SimpleDependencyFactory.getInstance();
        this.running = true;
        System.out.println("INFO: MenuPrincipalDemo inicializado");
    }

    public void iniciar() {
        mostrarBienvenida();

        while (running) {
            try {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();
                procesarOpcion(opcion);
            } catch (Exception e) {
                System.err.println("Error inesperado: " + e.getMessage());
                System.out.println("ERROR: Error en menu principal: " + e.getMessage());
                pausar();
            }
        }

        mostrarDespedida();
        scanner.close();
    }

    private void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║              🐾 HAPPY FEET VETERINARIA 🐾                   ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Sistema de Gestión Veterinaria                 ║");
        System.out.println("║                    Versión Demo 2.0                         ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        pausar();
    }

    private void mostrarMenuPrincipal() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MENU PRINCIPAL                           ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                              ║");
        System.out.println("║  👥  1. Gestión de Dueños                                   ║");
        System.out.println("║  🐕  2. Gestión de Mascotas                                 ║");
        System.out.println("║  📅  3. Gestión de Citas                                    ║");
        System.out.println("║  💰  4. Gestión de Facturas                                 ║");
        System.out.println("║  📦  5. Gestión de Inventario                               ║");
        System.out.println("║  📊  6. Reportes y Estadísticas                             ║");
        System.out.println("║  🎯  7. Actividades Especiales                              ║");
        System.out.println("║  ⚙️   8. Configuración y Utilidades                         ║");
        System.out.println("║  ❓  9. Ayuda                                                ║");
        System.out.println("║  🚪  0. Salir                                                ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.print("Seleccione una opción (0-9): ");
    }

    private int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar buffer
            System.err.println("❌ Error: Debe ingresar un número válido");
            pausar();
            return -1; // Opción inválida
        }
    }

    private void procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    mostrarSubtitulo("GESTIÓN DE DUEÑOS");
                    factory.getDuenoController().run();
                    break;
                case 2:
                    mostrarSubtitulo("GESTIÓN DE MASCOTAS");
                    factory.getMascotaController().run();
                    break;
                case 3:
                    mostrarSubtitulo("GESTIÓN DE CITAS");
                    factory.getCitaController().run();
                    break;
                case 4:
                    mostrarSubtitulo("GESTIÓN DE FACTURAS");
                    factory.getFacturaController().run();
                    break;
                case 5:
                    mostrarSubtitulo("GESTIÓN DE INVENTARIO");
                    factory.getInventarioController().run();
                    break;
                case 6:
                    mostrarSubtitulo("REPORTES Y ESTADÍSTICAS");
                    factory.getReporteController().menuReportes();
                    break;
                case 7:
                    mostrarSubtitulo("ACTIVIDADES ESPECIALES");
                    factory.getActividadesController().run();
                    break;
                case 8:
                    mostrarSubtitulo("CONFIGURACIÓN Y UTILIDADES");
                    mostrarMenuConfiguracion();
                    break;
                case 9:
                    mostrarAyuda();
                    break;
                case 0:
                    confirmarSalida();
                    break;
                default:
                    System.err.println("❌ Opción no válida. Por favor seleccione un número del 0 al 9.");
                    pausar();
                    break;
            }
        } catch (Exception e) {
            System.err.println("❌ Error al procesar la opción: " + e.getMessage());
            System.out.println("ERROR: Error procesando opción " + opcion + ": " + e.getMessage());
            pausar();
        }
    }

    private void mostrarMenuConfiguracion() {
        boolean enConfiguracion = true;

        while (enConfiguracion) {
            limpiarPantalla();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                CONFIGURACIÓN Y UTILIDADES                   ║");
            System.out.println("╠══════════════════════════════════════════════════════════════╣");
            System.out.println("║                                                              ║");
            System.out.println("║  🔧  1. Configuración del Sistema                           ║");
            System.out.println("║  🗄️   2. Gestión de Base de Datos                           ║");
            System.out.println("║  📄  3. Generar Factura Demo                                ║");
            System.out.println("║  📋  4. Reporte de Inventario Demo                          ║");
            System.out.println("║  🔍  5. Diagnóstico del Sistema                             ║");
            System.out.println("║  📝  6. Ver Logs del Sistema                                ║");
            System.out.println("║  🔙  0. Volver al Menú Principal                            ║");
            System.out.println("║                                                              ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.print("Seleccione una opción (0-6): ");

            try {
                int opcion = leerOpcion();
                switch (opcion) {
                    case 1:
                        mostrarConfiguracionSistema();
                        break;
                    case 2:
                        System.out.println("INFO: Función de base de datos en modo demo");
                        pausar();
                        break;
                    case 3:
                        generarFacturaDemo();
                        break;
                    case 4:
                        generarReporteInventarioDemo();
                        break;
                    case 5:
                        mostrarDiagnosticoSistema();
                        break;
                    case 6:
                        mostrarLogs();
                        break;
                    case 0:
                        enConfiguracion = false;
                        break;
                    default:
                        System.err.println("❌ Opción no válida. Por favor seleccione un número del 0 al 6.");
                        pausar();
                        break;
                }
            } catch (Exception e) {
                System.err.println("❌ Error en configuración: " + e.getMessage());
                pausar();
            }
        }
    }

    private void mostrarConfiguracionSistema() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                CONFIGURACIÓN DEL SISTEMA                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("📋 Estado del Sistema (Modo Demo):");
        System.out.println("   • Base de Datos: Simulada");
        System.out.println("   • Servicios: Activos (Mock)");
        System.out.println("   • Logging: Simplificado");
        System.out.println("   • Encoding: UTF-8");
        System.out.println();
        System.out.println("⚙️ Configuraciones disponibles:");
        System.out.println("   • Sistema en modo demostración");
        System.out.println("   • Para funcionalidad completa, configure la base de datos");
        System.out.println();
        pausar();
    }

    private void generarFacturaDemo() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   GENERANDO FACTURA DEMO                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            com.happyfeet.model.entities.Factura f = new com.happyfeet.model.entities.Factura();
            f.setId(1);
            f.setDuenoId(1);

            java.util.List<com.happyfeet.model.entities.ItemFactura> items = new java.util.ArrayList<>();
            com.happyfeet.model.entities.ItemFactura it = new com.happyfeet.model.entities.ItemFactura();
            it.setTipoItem(com.happyfeet.model.entities.ItemFactura.TipoItem.SERVICIO);
            it.setDescripcion("Consulta Veterinaria General - Demo");
            it.setCantidad(java.math.BigDecimal.valueOf(1));
            it.setPrecioUnitario(new java.math.BigDecimal("30.00"));
            it.calcularSubtotal();
            items.add(it);

            String path = "factura_demo_menu_" + System.currentTimeMillis() + ".txt";
            String output = com.happyfeet.util.InvoiceGenerator.generatePlainInvoice(f, items, path);

            if (output != null) {
                System.out.println("✅ Factura demo generada exitosamente");
                System.out.println("📄 Archivo: " + output);
                System.out.println("💰 Total: $33.60 (incluye impuestos)");
            } else {
                System.err.println("❌ Error al generar la factura demo");
            }
        } catch (Exception e) {
            System.err.println("❌ Error generando factura: " + e.getMessage());
        }

        System.out.println();
        pausar();
    }

    private void generarReporteInventarioDemo() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              REPORTE DE INVENTARIO DEMO                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            java.util.List<com.happyfeet.model.entities.Inventario> list = new java.util.ArrayList<>();
            java.util.List<com.happyfeet.model.entities.Inventario> low = com.happyfeet.util.Reports.lowStock(list, 5);

            System.out.println("📊 Reporte de Inventario (Demo):");
            System.out.println("   • Total de productos: " + list.size());
            System.out.println("   • Productos con stock bajo: " + low.size());
            System.out.println("   • Estado general: Sistema operativo en modo demo");
            System.out.println();
            System.out.println("ℹ️  Este es un reporte demo sin datos reales.");
        } catch (Exception e) {
            System.err.println("❌ Error generando reporte: " + e.getMessage());
        }

        System.out.println();
        pausar();
    }

    private void mostrarDiagnosticoSistema() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                DIAGNÓSTICO DEL SISTEMA                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        System.out.println("🔍 Verificando componentes del sistema (Demo)...");
        System.out.println();

        // Verificar memoria
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;

        System.out.printf("💾 Memoria:\n");
        System.out.printf("   • Total: %.2f MB\n", totalMemory / 1024.0 / 1024.0);
        System.out.printf("   • Usada: %.2f MB\n", usedMemory / 1024.0 / 1024.0);
        System.out.printf("   • Libre: %.2f MB\n", freeMemory / 1024.0 / 1024.0);
        System.out.println();

        System.out.println("🏥 Servicios (Mock):");
        System.out.println("   ✅ DuenoService: Operativo (demo)");
        System.out.println("   ✅ MascotaService: Operativo (demo)");
        System.out.println("   ✅ CitaService: Operativo (demo)");
        System.out.println("   ✅ FacturaService: Operativo (demo)");
        System.out.println("   ✅ InventarioService: Operativo (demo)");
        System.out.println("   ✅ ReporteService: Operativo (demo)");
        System.out.println();

        System.out.println("🗂️ Controladores:");
        System.out.println("   ✅ Todos los controladores inicializados correctamente");
        System.out.println();

        System.out.println("📊 Estado General: ✅ SISTEMA OPERATIVO (MODO DEMO)");
        System.out.println();
        pausar();
    }

    private void mostrarLogs() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LOGS DEL SISTEMA                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            System.out.println("📝 Últimas entradas del log (demo):");
            System.out.println("   • " + new java.util.Date() + " - Sistema iniciado en modo demo");
            System.out.println("   • " + new java.util.Date() + " - MenuPrincipalDemo cargado");
            System.out.println("   • " + new java.util.Date() + " - SimpleDependencyFactory inicializado");
            System.out.println("   • " + new java.util.Date() + " - Todos los servicios mock operativos");
            System.out.println();
            System.out.println("ℹ️  Sistema funcionando en modo demostración");
        } catch (Exception e) {
            System.err.println("❌ Error accediendo a logs: " + e.getMessage());
        }

        System.out.println();
        pausar();
    }

    private void mostrarAyuda() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                        AYUDA                                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🆘 Sistema de Ayuda - Happy Feet Veterinaria (Demo)");
        System.out.println();
        System.out.println("📋 Guía rápida de uso:");
        System.out.println();
        System.out.println("1️⃣  DUEÑOS: Registre y gestione información de propietarios");
        System.out.println("2️⃣  MASCOTAS: Administre datos de las mascotas y su historial");
        System.out.println("3️⃣  CITAS: Programe y gestione citas veterinarias");
        System.out.println("4️⃣  FACTURAS: Genere y administre facturas de servicios");
        System.out.println("5️⃣  INVENTARIO: Controle stock de medicamentos y productos");
        System.out.println("6️⃣  REPORTES: Genere estadísticas y reportes del negocio");
        System.out.println("7️⃣  ACTIVIDADES: Gestione adopciones, vacunaciones y eventos");
        System.out.println("8️⃣  CONFIGURACIÓN: Ajustes del sistema y utilidades");
        System.out.println();
        System.out.println("💡 Modo Demo:");
        System.out.println("   • Este sistema funciona en modo demostración");
        System.out.println("   • No se requiere base de datos para la navegación");
        System.out.println("   • Todas las funciones principales están disponibles");
        System.out.println("   • Para funcionalidad completa, configure MySQL");
        System.out.println();
        System.out.println("❓ Para soporte técnico, contacte al administrador del sistema");
        System.out.println();
        pausar();
    }

    private void confirmarSalida() {
        System.out.println();
        System.out.print("❓ ¿Está seguro que desea salir del sistema? (s/N): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
            running = false;
            System.out.println("INFO: Usuario salió del sistema demo");
        } else {
            System.out.println("✅ Continuando en el sistema...");
            pausar();
        }
    }

    private void mostrarDespedida() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║                 ¡GRACIAS POR PROBAR                         ║");
        System.out.println("║              HAPPY FEET VETERINARIA!                        ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Cuidando a sus mascotas con amor               ║");
        System.out.println("║                    (Versión Demo)                           ║");
        System.out.println("║                                                              ║");
        System.out.println("║                    🐾 Hasta pronto 🐾                       ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void mostrarSubtitulo(String titulo) {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.printf("║%s║%n", centrarTexto(titulo, 62));
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private String centrarTexto(String texto, int ancho) {
        if (texto.length() >= ancho) {
            return " " + texto.substring(0, ancho - 2) + " ";
        }

        int espacios = ancho - texto.length();
        int espaciosIzq = espacios / 2;
        int espaciosDer = espacios - espaciosIzq;

        return " ".repeat(espaciosIzq) + texto + " ".repeat(espaciosDer);
    }

    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Si no se puede limpiar la pantalla, simplemente agregar líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void pausar() {
        System.out.print("Presione ENTER para continuar...");
        scanner.nextLine();
    }

    // Método main para ejecutar el sistema
    public static void main(String[] args) {
        try {
            MenuPrincipalDemo menu = new MenuPrincipalDemo();
            menu.iniciar();
        } catch (Exception e) {
            System.err.println("❌ Error crítico del sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}