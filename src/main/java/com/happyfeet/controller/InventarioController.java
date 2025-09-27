package com.happyfeet.controller;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.InventarioRepository;
import com.happyfeet.service.InventarioService;
import com.happyfeet.service.impl.AlertaInventarioServiceObserver;
import com.happyfeet.util.FileLogger;
import com.happyfeet.view.ConsoleUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class InventarioController {

    private static final FileLogger LOG = FileLogger.getInstance();

    private final InventarioService inventarioService;
    private final InventarioRepository inventarioRepository;
    private final AlertaInventarioServiceObserver alertaService;

    public InventarioController(InventarioService inventarioService,
                                InventarioRepository inventarioRepository) {
        this.inventarioService = inventarioService;
        this.inventarioRepository = inventarioRepository;
        this.alertaService = new AlertaInventarioServiceObserver();
        LOG.info("InventarioController inicializado");
    }

    // ============ OPERACIONES PRINCIPALES ============

    public void listarTodosLosProductos() {
        try {
            List<Inventario> productos = inventarioRepository.findAll();
            if (productos.isEmpty()) {
                System.out.println("No hay productos en el inventario");
                return;
            }

            System.out.println("\n=== INVENTARIO COMPLETO ===");
            System.out.printf("%-5s %-15s %-30s %-10s %-8s %-8s %-12s%n",
                    "ID", "CÓDIGO", "PRODUCTO", "PRECIO", "STOCK", "MIN", "VENCIMIENTO");
            System.out.println("-".repeat(95));

            for (Inventario producto : productos) {
                // Verificar alertas mientras mostramos
                alertaService.notificar(producto);

                String fechaVenc = producto.getFechaVencimiento() != null ?
                        producto.getFechaVencimiento().toString() : "N/A";

                System.out.printf("%-5d %-15s %-30s $%-9.2f %-8d %-8d %-12s%n",
                        producto.getId(),
                        producto.getCodigo() != null ? producto.getCodigo() : "N/A",
                        producto.getNombreProducto(),
                        producto.getPrecioVenta(),
                        producto.getCantidadStock(),
                        producto.getStockMinimo(),
                        fechaVenc);
            }
        } catch (Exception e) {
            LOG.error("Error listando productos", e);
            System.err.println("Error al listar productos: " + e.getMessage());
        }
    }

    public void buscarProductoPorCodigo() {
        try {
            String codigo = ConsoleUtils.readNonEmpty("Código del producto");
            Optional<Inventario> producto = inventarioRepository.findByCodigo(codigo);

            if (producto.isPresent()) {
                mostrarDetalleProducto(producto.get());
                alertaService.notificar(producto.get());
            } else {
                System.out.println("No se encontró producto con código: " + codigo);
            }
        } catch (Exception e) {
            LOG.error("Error buscando producto por código", e);
            System.err.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public void buscarProductosPorNombre() {
        try {
            String nombre = ConsoleUtils.readNonEmpty("Nombre del producto (o parte)");
            List<Inventario> productos = inventarioRepository.findByNombreProducto(nombre);

            if (productos.isEmpty()) {
                System.out.println("No se encontraron productos con: " + nombre);
                return;
            }

            System.out.println("\n=== RESULTADOS DE BÚSQUEDA ===");
            for (Inventario producto : productos) {
                System.out.printf("ID: %d - %s (Código: %s) - Stock: %d - $%.2f%n",
                        producto.getId(),
                        producto.getNombreProducto(),
                        producto.getCodigo(),
                        producto.getCantidadStock(),
                        producto.getPrecioVenta());
            }
        } catch (Exception e) {
            LOG.error("Error buscando productos por nombre", e);
            System.err.println("Error en la búsqueda: " + e.getMessage());
        }
    }

    public void crearProducto() {
        try {
            String codigo = ConsoleUtils.readNonEmpty("Código del producto");

            // Verificar que no exista
            if (inventarioRepository.findByCodigo(codigo).isPresent()) {
                System.out.println("Ya existe un producto con ese código: " + codigo);
                return;
            }

            String nombre = ConsoleUtils.readNonEmpty("Nombre del producto");
            BigDecimal precio = new BigDecimal(ConsoleUtils.readDouble("Precio de venta"));
            int cantidadInicial = ConsoleUtils.readInt("Cantidad inicial en stock");
            int stockMinimo = ConsoleUtils.readInt("Stock mínimo");

            System.out.println("¿Tiene fecha de vencimiento? (s/n)");
            LocalDate fechaVencimiento = null;
            if (ConsoleUtils.confirm("")) {
                fechaVencimiento = ConsoleUtils.readDate("Fecha de vencimiento");
            }

            Inventario nuevoProducto = new Inventario();
            nuevoProducto.setCodigo(codigo);
            nuevoProducto.setNombreProducto(nombre);
            nuevoProducto.setPrecioVenta(precio);
            nuevoProducto.setCantidadStock(cantidadInicial);
            nuevoProducto.setStockMinimo(stockMinimo);
            nuevoProducto.setFechaVencimiento(fechaVencimiento);

            Inventario creado = inventarioRepository.save(nuevoProducto);
            LOG.info("Producto creado: " + creado.getNombreProducto() + " (ID: " + creado.getId() + ")");
            System.out.println("Producto creado exitosamente: " + creado.getNombreProducto());

        } catch (Exception e) {
            LOG.error("Error creando producto", e);
            System.err.println("Error al crear producto: " + e.getMessage());
        }
    }

    public void actualizarProducto() {
        try {
            int id = ConsoleUtils.readInt("ID del producto a actualizar");
            Optional<Inventario> productoOpt = inventarioRepository.findById(id);

            if (productoOpt.isEmpty()) {
                System.out.println("No se encontró producto con ID: " + id);
                return;
            }

            Inventario producto = productoOpt.get();
            System.out.println("Producto actual: " + producto.getNombreProducto());

            String nuevoNombre = ConsoleUtils.readOptional("Nuevo nombre (enter para mantener)");
            String nuevoPrecioStr = ConsoleUtils.readOptional("Nuevo precio (enter para mantener)");
            String nuevoStockMinimoStr = ConsoleUtils.readOptional("Nuevo stock mínimo (enter para mantener)");

            if (!nuevoNombre.isEmpty()) {
                producto.setNombreProducto(nuevoNombre);
            }
            if (!nuevoPrecioStr.isEmpty()) {
                producto.setPrecioVenta(new BigDecimal(nuevoPrecioStr));
            }
            if (!nuevoStockMinimoStr.isEmpty()) {
                producto.setStockMinimo(Integer.parseInt(nuevoStockMinimoStr));
            }

            inventarioRepository.update(producto);
            LOG.info("Producto actualizado: " + producto.getNombreProducto() + " (ID: " + id + ")");
            System.out.println("Producto actualizado exitosamente");

        } catch (Exception e) {
            LOG.error("Error actualizando producto", e);
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
    }

    public void eliminarProducto() {
        try {
            int id = ConsoleUtils.readInt("ID del producto a eliminar");
            Optional<Inventario> productoOpt = inventarioRepository.findById(id);

            if (productoOpt.isEmpty()) {
                System.out.println("No se encontró producto con ID: " + id);
                return;
            }

            Inventario producto = productoOpt.get();
            System.out.println("Producto: " + producto.getNombreProducto());
            System.out.println("Stock actual: " + producto.getCantidadStock());

            if (!ConsoleUtils.confirm("¿Confirma la eliminación?")) {
                System.out.println("Eliminación cancelada");
                return;
            }

            boolean eliminado = inventarioRepository.deleteById(id);
            if (eliminado) {
                LOG.info("Producto eliminado: " + producto.getNombreProducto() + " (ID: " + id + ")");
                System.out.println("Producto eliminado exitosamente");
            } else {
                System.out.println("No se pudo eliminar el producto");
            }

        } catch (Exception e) {
            LOG.error("Error eliminando producto", e);
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
    }

    // ============ GESTIÓN DE STOCK ============

    public void ajustarStock() {
        try {
            int id = ConsoleUtils.readInt("ID del producto");
            Optional<Inventario> productoOpt = inventarioRepository.findById(id);

            if (productoOpt.isEmpty()) {
                System.out.println("No se encontró producto con ID: " + id);
                return;
            }

            Inventario producto = productoOpt.get();
            System.out.println("Producto: " + producto.getNombreProducto());
            System.out.println("Stock actual: " + producto.getCantidadStock());

            int nuevoStock = ConsoleUtils.readInt("Nuevo stock");
            if (nuevoStock < 0) {
                System.out.println("El stock no puede ser negativo");
                return;
            }

            int stockAnterior = producto.getCantidadStock();
            producto.setCantidadStock(nuevoStock);
            inventarioRepository.update(producto);

            LOG.info(String.format("Stock ajustado para %s: %d -> %d",
                    producto.getNombreProducto(), stockAnterior, nuevoStock));
            System.out.println("Stock actualizado exitosamente");

            // Verificar alertas después del ajuste
            alertaService.notificar(producto);

        } catch (Exception e) {
            LOG.error("Error ajustando stock", e);
            System.err.println("Error al ajustar stock: " + e.getMessage());
        }
    }

    public void descontarStock() {
        try {
            int id = ConsoleUtils.readInt("ID del producto");
            int cantidad = ConsoleUtils.readInt("Cantidad a descontar");

            inventarioService.descontarStock(id, cantidad);
            System.out.println("Stock descontado exitosamente");

            // Mostrar stock actualizado y verificar alertas
            Optional<Inventario> producto = inventarioRepository.findById(id);
            if (producto.isPresent()) {
                System.out.println("Stock actual: " + producto.get().getCantidadStock());
                alertaService.notificar(producto.get());
            }

        } catch (Exception e) {
            LOG.error("Error descontando stock", e);
            System.err.println("Error: " + e.getMessage());
        }
    }

    // ============ REPORTES Y ALERTAS ============

    public void mostrarProductosConStockBajo() {
        try {
            // Usar método personalizado del repository si existe, sino filtrar todos
            List<Inventario> productos = inventarioRepository.findAll().stream()
                    .filter(p -> p.getCantidadStock() <= p.getStockMinimo())
                    .toList();

            if (productos.isEmpty()) {
                System.out.println("No hay productos con stock bajo");
                return;
            }

            System.out.println("\n=== PRODUCTOS CON STOCK BAJO ===");
            System.out.printf("%-30s %-8s %-8s%n", "PRODUCTO", "STOCK", "MÍNIMO");
            System.out.println("-".repeat(50));

            for (Inventario producto : productos) {
                System.out.printf("%-30s %-8d %-8d%n",
                        producto.getNombreProducto().length() > 30 ?
                                producto.getNombreProducto().substring(0, 27) + "..." : producto.getNombreProducto(),
                        producto.getCantidadStock(),
                        producto.getStockMinimo());
                alertaService.onStockBajo(producto);
            }
        } catch (Exception e) {
            LOG.error("Error mostrando productos con stock bajo", e);
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }

    public void mostrarProductosVencidos() {
        try {
            List<Inventario> productos = inventarioRepository.findAll().stream()
                    .filter(Inventario::estaVencido)
                    .toList();

            if (productos.isEmpty()) {
                System.out.println("No hay productos vencidos");
                return;
            }

            System.out.println("\n=== PRODUCTOS VENCIDOS ===");
            System.out.printf("%-30s %-12s %-8s%n", "PRODUCTO", "VENCIMIENTO", "STOCK");
            System.out.println("-".repeat(55));

            for (Inventario producto : productos) {
                System.out.printf("%-30s %-12s %-8d%n",
                        producto.getNombreProducto().length() > 30 ?
                                producto.getNombreProducto().substring(0, 27) + "..." : producto.getNombreProducto(),
                        producto.getFechaVencimiento(),
                        producto.getCantidadStock());
                alertaService.onProductoVencido(producto);
            }
        } catch (Exception e) {
            LOG.error("Error mostrando productos vencidos", e);
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }

    public void mostrarProductosPorVencer() {
        try {
            int diasAviso = ConsoleUtils.readInt("Días de aviso (por defecto 7)");
            if (diasAviso <= 0) diasAviso = 7;

            LocalDate fechaLimite = LocalDate.now().plusDays(diasAviso);
            List<Inventario> productos = inventarioRepository.findAll().stream()
                    .filter(p -> p.getFechaVencimiento() != null &&
                            !p.estaVencido() &&
                            p.getFechaVencimiento().isBefore(fechaLimite))
                    .toList();

            if (productos.isEmpty()) {
                System.out.println("No hay productos próximos a vencer en " + diasAviso + " días");
                return;
            }

            System.out.println("\n=== PRODUCTOS POR VENCER EN " + diasAviso + " DÍAS ===");
            System.out.printf("%-30s %-12s %-8s%n", "PRODUCTO", "VENCIMIENTO", "STOCK");
            System.out.println("-".repeat(55));

            for (Inventario producto : productos) {
                System.out.printf("%-30s %-12s %-8d%n",
                        producto.getNombreProducto().length() > 30 ?
                                producto.getNombreProducto().substring(0, 27) + "..." : producto.getNombreProducto(),
                        producto.getFechaVencimiento(),
                        producto.getCantidadStock());
                alertaService.onProductoPorVencer(producto);
            }
        } catch (Exception e) {
            LOG.error("Error mostrando productos por vencer", e);
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }

    public void generarReporteCompleto() {
        System.out.println("\n=== REPORTE COMPLETO DE INVENTARIO ===");

        // Resumen general
        List<Inventario> todosProductos = inventarioRepository.findAll();
        int totalProductos = todosProductos.size();
        int stockTotal = todosProductos.stream().mapToInt(Inventario::getCantidadStock).sum();
        BigDecimal valorTotal = todosProductos.stream()
                .map(p -> p.getPrecioVenta().multiply(BigDecimal.valueOf(p.getCantidadStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total de productos: " + totalProductos);
        System.out.println("Stock total: " + stockTotal + " unidades");
        System.out.printf("Valor total del inventario: $%.2f%n", valorTotal);

        // Productos con alertas
        long stockBajo = todosProductos.stream().filter(p -> p.getCantidadStock() <= p.getStockMinimo()).count();
        long vencidos = todosProductos.stream().filter(Inventario::estaVencido).count();

        if (stockBajo > 0) {
            System.out.println("⚠️ Productos con stock bajo: " + stockBajo);
        }
        if (vencidos > 0) {
            System.out.println("❌ Productos vencidos: " + vencidos);
        }

        System.out.println("\nUse las opciones del menú para ver detalles de cada categoría.");
    }

    // ============ MÉTODOS AUXILIARES ============

    private void mostrarDetalleProducto(Inventario producto) {
        System.out.println("\n=== DETALLE DEL PRODUCTO ===");
        System.out.println("ID: " + producto.getId());
        System.out.println("Código: " + (producto.getCodigo() != null ? producto.getCodigo() : "N/A"));
        System.out.println("Nombre: " + producto.getNombreProducto());
        System.out.printf("Precio: $%.2f%n", producto.getPrecioVenta());
        System.out.println("Stock actual: " + producto.getCantidadStock());
        System.out.println("Stock mínimo: " + producto.getStockMinimo());
        System.out.println("Fecha vencimiento: " +
                (producto.getFechaVencimiento() != null ? producto.getFechaVencimiento() : "N/A"));

        if (producto.estaVencido()) {
            System.out.println("🚨 PRODUCTO VENCIDO");
        } else if (producto.getCantidadStock() <= producto.getStockMinimo()) {
            System.out.println("⚠️ STOCK BAJO");
        }
    }

    // Getters para testing
    public InventarioService getInventarioService() {
        return inventarioService;
    }

    public InventarioRepository getInventarioRepository() {
        return inventarioRepository;
    }

    // Métodos añadidos desde versión integrada

    
    
    public void run() {
        System.out.println("=== GESTIÓN DE INVENTARIO ===");
        System.out.println("Funcionalidad disponible:");
        System.out.println("- Agregar nuevos productos");
        System.out.println("- Controlar niveles de stock");
        System.out.println("- Alertas de stock bajo y productos vencidos");
        System.out.println("- Ajustar cantidades de inventario");
        System.out.println("- Buscar productos por código y nombre");
        System.out.println("- Generar reportes de inventario");
        System.out.println("- Gestionar fechas de vencimiento");
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
