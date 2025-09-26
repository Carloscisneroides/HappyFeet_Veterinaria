package com.happyfeet;

import com.happyfeet.util.DatabaseConnection;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== TEST CONEXIÓN Y CRUD ===");

        DatabaseConnection db = DatabaseConnection.getInstance();

        try {
            // Probar conexión
            System.out.println("Estado conexión: " + (db.isConnectionValid() ? "OK" : "Error"));

            // Test CREATE - Crear nueva especie
            crearEspecie(db, "Reptil", "Reptiles domésticos como iguanas y tortugas");

            // Test READ - Listar todas las especies
            listarEspecies(db);

            // Test UPDATE - Actualizar la especie recién creada
            actualizarEspecie(db, "Reptil", "Reptiles y anfibios domésticos");

            // Test DELETE - Eliminar especie
            // eliminarEspecie(db, "Reptil");

            System.out.println("\nPruebas completadas exitosamente");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }

    // CREATE
    private static void crearEspecie(DatabaseConnection db, String nombre, String descripcion) {
        String sql = "INSERT INTO especies (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);

            int filas = pstmt.executeUpdate();
            System.out.println("CREATE: " + (filas > 0 ? "Especie creada" : "Error al crear"));

        } catch (SQLException e) {
            System.err.println("Error CREATE: " + e.getMessage());
        }
    }

    // READ
    private static void listarEspecies(DatabaseConnection db) {
        String sql = "SELECT id, nombre, descripcion FROM especies";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nREAD: Lista de especies:");
            System.out.println("ID | Nombre      | Descripción");
            System.out.println("---+-------------+---------------------------");

            while (rs.next()) {
                System.out.printf("%-2d | %-11s | %s%n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error READ: " + e.getMessage());
        }
    }

    // UPDATE
    private static void actualizarEspecie(DatabaseConnection db, String nombre, String nuevaDescripcion) {
        String sql = "UPDATE especies SET descripcion = ? WHERE nombre = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaDescripcion);
            pstmt.setString(2, nombre);

            int filas = pstmt.executeUpdate();
            System.out.println("UPDATE: " + (filas > 0 ? "Especie actualizada" : "No encontrada"));

        } catch (SQLException e) {
            System.err.println("Error UPDATE: " + e.getMessage());
        }
    }

    // DELETE
    private static void eliminarEspecie(DatabaseConnection db, String nombre) {
        String sql = "DELETE FROM especies WHERE nombre = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);

            int filas = pstmt.executeUpdate();
            System.out.println("DELETE: " + (filas > 0 ? "Especie eliminada" : "No encontrada"));

        } catch (SQLException e) {
            System.err.println("Error DELETE: " + e.getMessage());
        }
    }
}