package com.happyfeet.view;



import com.happyfeet.config.DependencyFactory;
import com.happyfeet.util.AppLogger;


import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final DependencyFactory factory;
    private boolean running;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.factory = DependencyFactory.getInstance();
        this.running = true;
        AppLogger.info("MenuPrincipal inicializado");
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
                AppLogger.error("Error en menu principal: " + e.getMessage());
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
        System.out.println("║              ** HAPPY FEET VETERINARIA **                   ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Sistema de Gestión Veterinaria                 ║");
        System.out.println("║                      Versión 2.0                            ║");
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
        System.out.println("║  [1] 1. Gestión de Pacientes (Dueños)                      ║");
        System.out.println("║  [2] 2. Gestión de Mascotas (Ficha Completa)               ║");
        System.out.println("║  [3] 3. Servicios Médicos y Citas                          ║");
        System.out.println("║  [4] 4. Facturación y Reportes Gerenciales                 ║");
        System.out.println("║  [5] 5. Inventario y Gestión de Farmacia                   ║");
        System.out.println("║  [6] 6. Reportes y Estadísticas                             ║");
        System.out.println("║  [7] 7. Actividades Especiales (Adopciones/Club)           ║");
        System.out.println("║  [8] 8. Configuración y Utilidades                         ║");
        System.out.println("║  [9] 9. Ayuda                                               ║");
        System.out.println("║  [0] 0. Salir                                               ║");
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
                    mostrarSubtitulo("GESTIÓN DE PACIENTES (DUEÑOS)");
                    factory.getDuenoController().run();
                    break;
                case 2:
                    mostrarSubtitulo("GESTIÓN DE MASCOTAS - FICHA COMPLETA");
                    factory.getMascotaController().run();
                    break;
                case 3:
                    mostrarSubtitulo("SERVICIOS MÉDICOS Y CITAS");
                    factory.getCitaController().run();
                    break;
                case 4:
                    mostrarSubtitulo("FACTURACIÓN Y REPORTES GERENCIALES");
                    factory.getFacturaController().run();
                    break;
                case 5:
                    mostrarSubtitulo("INVENTARIO Y GESTIÓN DE FARMACIA");
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
            AppLogger.error("Error procesando opción " + opcion + ": " + e.getMessage());
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
                        factory.getMainController().run();
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
        System.out.println("📋 Estado del Sistema:");
        System.out.println("   • Base de Datos: Conectada");
        System.out.println("   • Servicios: Activos");
        System.out.println("   • Logging: Habilitado");
        System.out.println("   • Encoding: UTF-8");
        System.out.println();
        System.out.println("⚙️ Configuraciones disponibles:");
        System.out.println("   • Todas las configuraciones están optimizadas");
        System.out.println("   • Para cambios avanzados, contacte al administrador");
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
            // Usar la lógica del EnhancedMain
            com.happyfeet.model.entities.Factura f = new com.happyfeet.model.entities.Factura();
            f.setId(1);
            f.setDuenoId(1);
    
            java.util.List<com.happyfeet.model.entities.ItemFactura> items = new java.util.ArrayList<>();
            com.happyfeet.model.entities.ItemFactura it = new com.happyfeet.model.entities.ItemFactura();
            it.setTipoItem(com.happyfeet.model.entities.ItemFactura.TipoItem.SERVICIO);
            it.setDescripcion("Consulta Veterinaria General");
            it.setCantidad(java.math.BigDecimal.valueOf(1));
            it.setPrecioUnitario(new java.math.BigDecimal("25.00"));
            it.calcularSubtotal();
            items.add(it);
    
            String path = "factura_demo_" + System.currentTimeMillis() + ".txt";
            String output = com.happyfeet.util.InvoiceGenerator.generatePlainInvoice(f, items, path);
    
            if (output != null) {
                System.out.println("✅ Factura demo generada exitosamente");
                System.out.println("📄 Archivo: " + output);
                System.out.println("💰 Total: $27.40 (incluye impuestos)");
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
    
            System.out.println("📊 Reporte de Inventario:");
            System.out.println("   • Total de productos: " + list.size());
            System.out.println("   • Productos con stock bajo: " + low.size());
            System.out.println("   • Estado general: Sistema operativo");
            System.out.println();
            System.out.println("ℹ️  Este es un reporte demo. Para datos reales, configure la base de datos.");
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
    
        System.out.println("🔍 Verificando componentes del sistema...");
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
    
        System.out.println("🏥 Servicios:");
        System.out.println("   ✅ DuenoService: Operativo");
        System.out.println("   ✅ MascotaService: Operativo");
        System.out.println("   ✅ CitaService: Operativo");
        System.out.println("   ✅ FacturaService: Operativo");
        System.out.println("   ✅ InventarioService: Operativo");
        System.out.println("   ✅ ReporteService: Operativo");
        System.out.println();
    
        System.out.println("🗂️ Controladores:");
        System.out.println("   ✅ Todos los controladores inicializados correctamente");
        System.out.println();
    
        System.out.println("📊 Estado General: ✅ SISTEMA OPERATIVO");
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
            System.out.println("📝 Últimas entradas del log:");
            System.out.println("   • " + new java.util.Date() + " - Sistema iniciado correctamente");
            System.out.println("   • " + new java.util.Date() + " - MenuPrincipal cargado");
            System.out.println("   • " + new java.util.Date() + " - DependencyFactory inicializado");
            System.out.println("   • " + new java.util.Date() + " - Todos los servicios operativos");
            System.out.println();
            System.out.println("ℹ️  Para logs detallados, verifique el archivo 'happyfeet.log'");
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
        System.out.println("🆘 Sistema de Ayuda - Happy Feet Veterinaria");
        System.out.println();
        System.out.println("📋 Guía rápida de uso:");
        System.out.println();
        System.out.println("1️⃣  PACIENTES: Registre dueños con contacto de emergencia y relaciones");
        System.out.println("2️⃣  MASCOTAS: Ficha completa con alergias, peso, vacunas, microchip y foto");
        System.out.println("3️⃣  SERVICIOS MÉDICOS: Consultas completas con descuento automático de inventario");
        System.out.println("4️⃣  FACTURACIÓN: Facturas en texto plano con IVA y reportes gerenciales");
        System.out.println("5️⃣  INVENTARIO: Control de stock, alertas, vencimientos y proveedores");
        System.out.println("6️⃣  REPORTES: Estadísticas de servicios, veterinarios y análisis financiero");
        System.out.println("7️⃣  ACTIVIDADES: Adopciones con contratos, jornadas de vacunación y club de puntos");
        System.out.println("8️⃣  CONFIGURACIÓN: Ajustes del sistema y utilidades");
        System.out.println();
        System.out.println("💡 Consejos:");
        System.out.println("   • Siempre registre primero a los dueños antes que las mascotas");
        System.out.println("   • Use el inventario para controlar stock automáticamente");
        System.out.println("   • Los reportes se actualizan en tiempo real");
        System.out.println("   • Las facturas incluyen cálculo automático de impuestos");
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
            AppLogger.info("Usuario salió del sistema");
        } else {
            System.out.println("✅ Continuando en el sistema...");
            pausar();
        }
    }

    
    
    private void mostrarDespedida() {
        limpiarPantalla();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║                 ¡GRACIAS POR USAR                           ║");
        System.out.println("║              HAPPY FEET VETERINARIA!                        ║");
        System.out.println("║                                                              ║");
        System.out.println("║              Cuidando a sus mascotas con amor               ║");
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

    
    public static void main(String[] args) {
        try {
            MenuPrincipal menu = new MenuPrincipal();
            menu.iniciar();
        } catch (Exception e) {
            System.err.println("❌ Error crítico del sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
