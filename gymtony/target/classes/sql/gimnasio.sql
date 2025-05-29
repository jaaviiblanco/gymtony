-- Crear la base de datos con codificación UTF-8
CREATE DATABASE IF NOT EXISTS gimnasio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos gimnasio
USE gimnasio;

-- Tabla de planes_gimnasio
CREATE TABLE planes_gimnasio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio_mensual DOUBLE NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(25) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    dni VARCHAR(9) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    age INT NOT NULL CHECK (age >= 16 AND age <= 100),
    role ENUM('monitor', 'responsable', 'cliente') NOT NULL DEFAULT 'cliente',
    especialidad VARCHAR(100), -- Para monitores y responsables
    plan_id BIGINT, -- Nuevo: relación con planes_gimnasio
    FOREIGN KEY (plan_id) REFERENCES planes_gimnasio(id) ON DELETE SET NULL,
    CHECK (
        role IN ('monitor', 'responsable') OR especialidad IS NULL
    )
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de clases
CREATE TABLE clases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    monitor_id BIGINT NOT NULL,
    duracion INT NOT NULL CHECK (duracion >= 1), -- Duración mínima de 1 minuto
    ubicacion VARCHAR(100) NOT NULL,
    plazas_maximas INT NOT NULL CHECK (plazas_maximas >= 1), -- Añadido campo para plazas
    FOREIGN KEY (monitor_id) REFERENCES usuarios(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de reservas
CREATE TABLE reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    clase_id BIGINT NOT NULL,
    fecha_reserva DATETIME NOT NULL,
    confirmada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de comentarios (mejorada)
CREATE TABLE comentarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    clase_id BIGINT NULL,
    monitor_id BIGINT NULL,
    tipo ENUM('clase', 'monitor') NOT NULL DEFAULT 'clase',
    texto TEXT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    fecha_comentario DATETIME NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE,
    FOREIGN KEY (monitor_id) REFERENCES usuarios(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;



-- Índices para optimizar las búsquedas en algunas columnas
CREATE INDEX idx_usuario_username ON usuarios(username);
CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_clase_monitor_id ON clases(monitor_id);
CREATE INDEX idx_reserva_usuario_id ON reservas(usuario_id);
CREATE INDEX idx_reserva_clase_id ON reservas(clase_id);
CREATE INDEX idx_comentarios_cliente_id ON comentarios(cliente_id);
CREATE INDEX idx_comentarios_clase_id ON comentarios(clase_id);
