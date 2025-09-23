package com.happyfeet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    // Bloque estático para registrar el driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Controlador MySQL registrado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al registrar el controlador MySQL: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {
        initializeConnection();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            Properties props = loadDatabaseProperties();
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");

            // Validar que tenemos los parámetros necesarios
            if (url == null || username == null) {
                throw new SQLException("Configuración de base de datos incompleta");
            }

            // Establecer conexión inicial
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión a BD establecida correctamente");

            // Verificar que la base de datos existe y es accesible
            testConnection();

        } catch (SQLException e) {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
            System.err.println("URL: " + url);
            System.err.println("Usuario: " + username);
            throw new RuntimeException("No se pudo establecer conexión con la base de datos", e);
        }
    }

    private Properties loadDatabaseProperties() {
        Properties props = new Properties();

        try {
            // Primero intentar cargar desde archivo properties
            File configFile = new File("database.properties");
            if (configFile.exists()) {
                try (InputStream input = new FileInputStream(configFile)) {
                    props.load(input);
                    System.out.println("Configuración cargada desde database.properties");
                }
            } else {
                // Si no existe, usar valores por defecto
                System.out.println("No se encontró database.properties, usando valores por defecto");
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/happy_feet_veterinaria");
                props.setProperty("db.username", "root");
                props.setProperty("db.password", "");

                // Crear archivo de configuración de ejemplo
                createExampleConfigFile();
            }
        } catch (IOException e) {
            System.err.println("Error al cargar configuración: " + e.getMessage());
            // Usar valores por defecto
            props.setProperty("db.url", "jdbc:mysql://localhost:3306/happy_feet_veterinaria");
            props.setProperty("db.username", "root");
            props.setProperty("db.password", "");
        }

        return props;
    }

    private void createExampleConfigFile() {
        try {
            Properties exampleProps = new Properties();
            exampleProps.setProperty("db.url", "jdbc:mysql://localhost:3306/happy_feet_veterinaria");
            exampleProps.setProperty("db.username", "root");
            exampleProps.setProperty("db.password", "tu_password_mysql");
            exampleProps.setProperty("db.connection.timeout", "30");

            // No guardamos el ejemplo automáticamente para evitar sobrescribir
            System.out.println("Crea un archivo 'database.properties' con la configuración de tu BD");
        } catch (Exception e) {
            System.err.println("Error al crear archivo de ejemplo: " + e.getMessage());
        }
    }

    private void testConnection() throws SQLException {
        try (var stmt = connection.createStatement()) {
            var rs = stmt.executeQuery("SELECT 1");
            if (rs.next()) {
                System.out.println("Test de conexión a BD exitoso");
            }
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (this) {
                    if (connection == null || connection.isClosed()) {
                        System.out.println("Reconectando a la base de datos...");
                        this.connection = DriverManager.getConnection(url, username, password);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
            throw new RuntimeException("Error de conexión a la base de datos", e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a BD cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

    // Método para verificar el estado de la conexión
    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }

    // Método para obtener información de la conexión (útil para debugging)
    public String getConnectionInfo() {
        try {
            if (connection != null && !connection.isClosed()) {
                var metaData = connection.getMetaData();
                return String.format("BD: %s, Versión: %s",
                        metaData.getDatabaseProductName(),
                        metaData.getDatabaseProductVersion());
            }
        } catch (SQLException e) {
            // Ignorar error en este contexto
        }
        return "Conexión no disponible";
    }
}