package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Inventario;
import com.happyfeet.repository.InventarioRepository;
import com.happyfeet.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventarioRepositoryImpl implements InventarioRepository {
    private final DatabaseConnection db;

    public InventarioRepositoryImpl() {
        this.db = DatabaseConnection.getInstance();
    }

    @Override
    public Inventario save(Inventario inventario) {
        String sql = "INSERT INTO inventario (codigo, nombre_producto, descripcion, categoria, precio_compra, precio_venta, cantidad_stock, stock_minimo, fecha_vencimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int i = 1;
            ps.setString(i++, inventario.getCodigo());
            ps.setString(i++, inventario.getNombreProducto());
            ps.setString(i++, "Producto"); // descripcion por defecto
            ps.setString(i++, "Medicamento"); // categoria por defecto
            ps.setBigDecimal(i++, inventario.getPrecioVenta()); // precio_compra = precio_venta por defecto
            ps.setBigDecimal(i++, inventario.getPrecioVenta());
            ps.setInt(i++, inventario.getCantidadStock() != null ? inventario.getCantidadStock() : 0);
            ps.setInt(i++, inventario.getStockMinimo() != null ? inventario.getStockMinimo() : 5);

            if (inventario.getFechaVencimiento() != null) {
                ps.setDate(i++, Date.valueOf(inventario.getFechaVencimiento()));
            } else {
                ps.setNull(i++, Types.DATE);
            }

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    inventario.setId(keys.getInt(1));
                }
            }
            return inventario;
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando producto en inventario", e);
        }
    }

    @Override
    public Optional<Inventario> findById(Integer id) {
        if (id == null) return Optional.empty();
        String sql = "SELECT * FROM inventario WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando producto por id", e);
        }
    }

    @Override
    public List<Inventario> findAll() {
        String sql = "SELECT * FROM inventario ORDER BY nombre_producto";
        List<Inventario> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error listando inventario", e);
        }
    }

    @Override
    public Inventario update(Inventario inventario) {
        if (inventario.getId() == null) throw new IllegalArgumentException("ID requerido para actualizar");
        String sql = "UPDATE inventario SET codigo=?, nombre_producto=?, precio_venta=?, cantidad_stock=?, stock_minimo=?, fecha_vencimiento=? WHERE id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, inventario.getCodigo());
            ps.setString(i++, inventario.getNombreProducto());
            ps.setBigDecimal(i++, inventario.getPrecioVenta());
            ps.setInt(i++, inventario.getCantidadStock() != null ? inventario.getCantidadStock() : 0);
            ps.setInt(i++, inventario.getStockMinimo() != null ? inventario.getStockMinimo() : 5);

            if (inventario.getFechaVencimiento() != null) {
                ps.setDate(i++, Date.valueOf(inventario.getFechaVencimiento()));
            } else {
                ps.setNull(i++, Types.DATE);
            }
            ps.setInt(i, inventario.getId());

            ps.executeUpdate();
            return inventario;
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando producto", e);
        }
    }

    @Override
    public boolean deleteById(Integer id) {
        if (id == null) return false;
        String sql = "DELETE FROM inventario WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando producto", e);
        }
    }

    @Override
    public Optional<Inventario> findByCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return Optional.empty();
        String sql = "SELECT * FROM inventario WHERE codigo = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando producto por código", e);
        }
    }

    @Override
    public List<Inventario> findByNombreProducto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return List.of();
        String sql = "SELECT * FROM inventario WHERE LOWER(nombre_producto) LIKE ? ORDER BY nombre_producto";
        List<Inventario> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre.trim().toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando productos por nombre", e);
        }
    }

    // Métodos adicionales específicos para inventario
    public List<Inventario> findProductosConStockBajo() {
        String sql = "SELECT * FROM inventario WHERE cantidad_stock <= stock_minimo ORDER BY cantidad_stock";
        List<Inventario> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando productos con stock bajo", e);
        }
    }

    public List<Inventario> findProductosVencidos() {
        String sql = "SELECT * FROM inventario WHERE fecha_vencimiento < CURDATE() ORDER BY fecha_vencimiento";
        List<Inventario> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando productos vencidos", e);
        }
    }

    public List<Inventario> findProductosPorVencer(int dias) {
        String sql = "SELECT * FROM inventario WHERE fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) ORDER BY fecha_vencimiento";
        List<Inventario> list = new ArrayList<>();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando productos por vencer", e);
        }
    }

    private Inventario mapRow(ResultSet rs) throws SQLException {
        Inventario inventario = new Inventario();
        inventario.setId(rs.getInt("id"));
        inventario.setCodigo(rs.getString("codigo"));
        inventario.setNombreProducto(rs.getString("nombre_producto"));
        inventario.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        inventario.setCantidadStock(rs.getInt("cantidad_stock"));
        inventario.setStockMinimo(rs.getInt("stock_minimo"));

        Date fechaVencimiento = rs.getDate("fecha_vencimiento");
        if (fechaVencimiento != null) {
            inventario.setFechaVencimiento(fechaVencimiento.toLocalDate());
        }

        return inventario;
    }
}