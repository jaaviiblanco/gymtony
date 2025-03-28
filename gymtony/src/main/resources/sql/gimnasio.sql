-- Crear la base de datos con codificación UTF-8
CREATE DATABASE IF NOT EXISTS gimnasio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos gimnasio
USE gimnasio;

-- Tabla de usuarios con codificación UTF-8
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(25) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    dni VARCHAR(9) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    age INT NOT NULL CHECK (age >= 16 AND age <= 100),
    role ENUM('MONITOR', 'RESPONSABLE', 'CLIENTE') NOT NULL DEFAULT 'CLIENTE'
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de categorías de clases con codificación UTF-8
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de monitores (hereda de usuarios) con codificación UTF-8
CREATE TABLE monitores (
    id INT PRIMARY KEY,
    especialidad VARCHAR(100),
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de clases con codificación UTF-8
CREATE TABLE clases (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria_id INT NOT NULL,
    monitor_id INT NOT NULL,
    descripcion TEXT,
    horario TIME NOT NULL,
    sala VARCHAR(50),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE,
    FOREIGN KEY (monitor_id) REFERENCES monitores(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de reservas con codificación UTF-8
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    clase_id INT NOT NULL,
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tabla de reseñas con codificación UTF-8
CREATE TABLE reseñas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    clase_id INT NOT NULL,
    monitor_id INT NOT NULL,
    reseña TEXT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (clase_id) REFERENCES clases(id) ON DELETE CASCADE,
    FOREIGN KEY (monitor_id) REFERENCES monitores(id) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;