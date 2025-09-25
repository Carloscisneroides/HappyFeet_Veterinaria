package com.happyfeet.repository.impl;

import com.happyfeet.model.entities.Dueno;
import com.happyfeet.repository.DuenoRepository;
import com.happyfeet.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class DuenoRepositoryImpl implements DuenoRepository {
    private final DatabaseConnection dbConnection;

    public DuenoRepositoryImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public List<Dueno> findAll() {
        List<Dueno> duenos = new ArrayList<>();
        String sql = "SELECT * FROM duenos";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LocalDate fechaNacimiento = null;
                var fechaNacimientoDate = rs.getDate("fecha_nacimiento");
                if (fechaNacimientoDate != null) {
                    fechaNacimiento = fechaNacimientoDate.toLocalDate();
                }

                LocalDateTime fechaRegistro = toLocalDateTimeSafe(rs.getTimestamp("fecha_registro"));
                LocalDateTime fechaActualizacion = toLocalDateTimeSafe(rs.getTimestamp("fecha_actualizacion"));

                Dueno dueno = Dueno.Builder.create()
                        .withId(rs.getInt("id"))
                        .withNombreCompleto(rs.getString("nombre_completo"))
                        .withDocumentoIdentidad(rs.getString("documento_identidad"))
                        .withDireccion(rs.getString("direccion"))
                        .withTelefono(rs.getString("telefono"))
                        .withEmail(rs.getString("email"))
                        .withContactoEmergencia(rs.getString("contacto_emergencia"))
                        .withFechaNacimiento(fechaNacimiento)
                        .withTipoSangre(rs.getString("tipo_sangre"))
                        .withAlergia(rs.getString("alergias"))
                        .withFechaRegistro(fechaRegistro)
                        .withFechaActualizacion(fechaActualizacion)
                        .build();

                duenos.add(dueno);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return duenos;
    }

    private LocalDateTime toLocalDateTimeSafe(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }
}

