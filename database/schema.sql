-- =========== ESQUEMA COMPLETO MEJORADO ===========
DROP DATABASE IF EXISTS happy_feet_veterinaria;
CREATE DATABASE happy_feet_veterinaria;
USE happy_feet_veterinaria;

-- =========== TABLAS DE CONFIGURACIÓN ===========
CREATE TABLE especies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE razas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    especie_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    caracteristicas TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (especie_id) REFERENCES especies(id) ON DELETE CASCADE,
    UNIQUE KEY unique_raza_especie (especie_id, nombre)
);

CREATE TABLE producto_tipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evento_tipos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    requiere_diagnostico BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cita_estados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT,
    es_activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========== TABLAS MAESTRAS ===========
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    ruc VARCHAR(20) UNIQUE,
    contacto VARCHAR(255),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE veterinarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(20) UNIQUE NOT NULL,
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    direccion TEXT,
    fecha_contratacion DATE,
    salario DECIMAL(10,2),
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE servicios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    duracion_estimada INT DEFAULT 30 COMMENT 'minutos',
    categoria ENUM('Consulta', 'Procedimiento', 'Cirugía', 'Estética') DEFAULT 'Consulta',
    requiere_equipo_especial BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========== TABLAS OPERACIONALES ===========
CREATE TABLE duenos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(20) UNIQUE NOT NULL,
    direccion TEXT,
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    contacto_emergencia VARCHAR(20),
    fecha_nacimiento DATE,
    tipo_sangre VARCHAR(5),
    alergias TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_documento (documento_identidad),
    INDEX idx_email (email),
    INDEX idx_nombre (nombre_completo)
);

CREATE TABLE mascotas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    raza_id INT NOT NULL,
    fecha_nacimiento DATE,
    sexo ENUM('Macho', 'Hembra') NOT NULL,
    color VARCHAR(100),
    senias_particulares TEXT,
    url_foto VARCHAR(500),
    alergias TEXT,
    condiciones_preexistentes TEXT,
    peso_actual DECIMAL(5,2),
    microchip VARCHAR(50) UNIQUE,
    fecha_implantacion_microchip DATE,
    agresivo BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dueno_id) REFERENCES duenos(id) ON DELETE CASCADE,
    FOREIGN KEY (raza_id) REFERENCES razas(id),
    INDEX idx_dueno (dueno_id),
    INDEX idx_nombre (nombre),
    INDEX idx_microchip (microchip)
);

CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    mascota_id INT NOT NULL,
    servicio_id INT NOT NULL,
    veterinario_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    motivo_consulta TEXT NOT NULL,
    estado_id INT NOT NULL DEFAULT 1,
    urgencia ENUM('Baja', 'Media', 'Alta') DEFAULT 'Baja',
    notas_previas TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE,
    FOREIGN KEY (servicio_id) REFERENCES servicios(id),
    FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id),
    FOREIGN KEY (estado_id) REFERENCES cita_estados(id),
    INDEX idx_fecha (fecha_hora),
    INDEX idx_estado (estado_id),
    INDEX idx_veterinario (veterinario_id),
    INDEX idx_mascota (mascota_id),
    UNIQUE KEY unique_cita_veterinario (veterinario_id, fecha_hora)
);

CREATE TABLE historial_medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mascota_id INT NOT NULL,
    cita_id INT NULL,
    evento_tipo_id INT NOT NULL,
    fecha_evento DATETIME NOT NULL,
    veterinario_id INT NOT NULL,
    temperatura DECIMAL(4,2),
    frecuencia_cardiaca INT,
    frecuencia_respiratoria INT,
    peso DECIMAL(5,2),
    sintomas TEXT,
    diagnostico TEXT,
    tratamiento_prescrito TEXT,
    medicamentos_recetados TEXT,
    observaciones TEXT,
    recomendaciones TEXT,
    fecha_proximo_control DATE,
    requiere_seguimiento BOOLEAN DEFAULT FALSE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id) ON DELETE CASCADE,
    FOREIGN KEY (cita_id) REFERENCES citas(id),
    FOREIGN KEY (evento_tipo_id) REFERENCES evento_tipos(id),
    FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id),
    INDEX idx_mascota (mascota_id),
    INDEX idx_fecha (fecha_evento),
    INDEX idx_veterinario (veterinario_id)
);

CREATE TABLE inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre_producto VARCHAR(255) NOT NULL,
    producto_tipo_id INT NOT NULL,
    proveedor_id INT NULL,
    descripcion TEXT,
    fabricante VARCHAR(100),
    lote VARCHAR(50),
    ubicacion VARCHAR(100),
    cantidad_stock INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    stock_maximo INT NULL,
    fecha_vencimiento DATE,
    precio_compra DECIMAL(10, 2),
    precio_venta DECIMAL(10, 2) NOT NULL,
    requiere_refrigeracion BOOLEAN DEFAULT FALSE,
    controlado BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_tipo_id) REFERENCES producto_tipos(id),
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id),
    INDEX idx_nombre (nombre_producto),
    INDEX idx_vencimiento (fecha_vencimiento),
    INDEX idx_stock (cantidad_stock),
    INDEX idx_activo (activo)
);

CREATE TABLE movimientos_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    tipo_movimiento ENUM('entrada', 'salida', 'ajuste', 'vencimiento') NOT NULL,
    cantidad INT NOT NULL,
    cantidad_anterior INT NOT NULL,
    cantidad_nueva INT NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    referencia_id INT NULL COMMENT 'ID de factura, cita, etc.',
    referencia_tipo VARCHAR(50) NULL COMMENT 'factura, cita, etc.',
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_registro VARCHAR(100),
    FOREIGN KEY (producto_id) REFERENCES inventario(id),
    INDEX idx_producto (producto_id),
    INDEX idx_fecha (fecha_movimiento),
    INDEX idx_tipo (tipo_movimiento)
);

CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(20) UNIQUE NOT NULL,
    dueno_id INT NOT NULL,
    fecha_emision DATETIME NOT NULL,
    fecha_vencimiento DATE,
    subtotal DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    impuestos DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    descuento DECIMAL(10, 2) DEFAULT 0.00,
    total DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    estado ENUM('Pendiente', 'Pagada', 'Cancelada', 'Vencida') DEFAULT 'Pendiente',
    forma_pago ENUM('Efectivo', 'Tarjeta Débito', 'Tarjeta Crédito', 'Transferencia') DEFAULT 'Efectivo',
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (dueno_id) REFERENCES duenos(id),
    INDEX idx_dueno (dueno_id),
    INDEX idx_fecha (fecha_emision),
    INDEX idx_estado (estado),
    INDEX idx_numero (numero_factura)
);

CREATE TABLE items_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    tipo_item ENUM('servicio', 'producto') NOT NULL,
    servicio_id INT NULL,
    producto_id INT NULL,
    descripcion VARCHAR(500) NOT NULL,
    cantidad DECIMAL(10,3) NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    descuento_item DECIMAL(10, 2) DEFAULT 0.00,
    subtotal DECIMAL(10, 2) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    FOREIGN KEY (servicio_id) REFERENCES servicios(id),
    FOREIGN KEY (producto_id) REFERENCES inventario(id),
    CHECK (
        (tipo_item = 'servicio' AND servicio_id IS NOT NULL) OR
        (tipo_item = 'producto' AND producto_id IS NOT NULL)
    ),
    INDEX idx_factura (factura_id)
);

CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    metodo_pago ENUM('Efectivo', 'Tarjeta Débito', 'Tarjeta Crédito', 'Transferencia') NOT NULL,
    referencia_pago VARCHAR(100),
    estado ENUM('Completado', 'Pendiente', 'Fallido') DEFAULT 'Completado',
    observaciones TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (factura_id) REFERENCES facturas(id),
    INDEX idx_factura (factura_id),
    INDEX idx_fecha (fecha_pago)
);

-- =========== SISTEMA DE USUARIOS Y SEGURIDAD ===========
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    rol ENUM('superadmin', 'admin', 'veterinario', 'recepcion', 'inventario') DEFAULT 'recepcion',
    veterinario_id INT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_ultimo_login TIMESTAMP NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_rol (rol)
);

CREATE TABLE sesiones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    token_sesion VARCHAR(255) UNIQUE NOT NULL,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMP NOT NULL,
    direccion_ip VARCHAR(45),
    user_agent TEXT,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_token (token_sesion),
    INDEX idx_usuario (usuario_id)
);

-- =========== TABLAS ADICIONALES ===========
CREATE TABLE puntos_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dueno_id INT NOT NULL UNIQUE,
    puntos_acumulados INT NOT NULL DEFAULT 0,
    puntos_redimidos INT NOT NULL DEFAULT 0,
    nivel ENUM('Bronce', 'Plata', 'Oro', 'Platino') DEFAULT 'Bronce',
    fecha_ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (dueno_id) REFERENCES duenos(id) ON DELETE CASCADE,
    INDEX idx_dueno (dueno_id),
    INDEX idx_nivel (nivel)
);

CREATE TABLE recordatorios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('vacuna', 'desparasitacion', 'control', 'cita', 'pago') NOT NULL,
    mascota_id INT NULL,
    dueno_id INT NULL,
    cita_id INT NULL,
    fecha_recordatorio DATETIME NOT NULL,
    mensaje TEXT NOT NULL,
    enviado BOOLEAN DEFAULT FALSE,
    fecha_envio TIMESTAMP NULL,
    metodo_envio ENUM('email', 'sms', 'sistema') DEFAULT 'sistema',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id),
    FOREIGN KEY (dueno_id) REFERENCES duenos(id),
    FOREIGN KEY (cita_id) REFERENCES citas(id),
    INDEX idx_fecha (fecha_recordatorio),
    INDEX idx_tipo (tipo),
    INDEX idx_enviado (enviado)
);

