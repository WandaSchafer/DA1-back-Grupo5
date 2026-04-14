-- =====================================================
-- SCHEMA MYSQL PARA BACKEND DA1 - ACTIVIDADES Y RESERVAS
-- =====================================================
-- Este script contiene todas las tablas necesarias para
-- las funcionalidades de actividades, usuarios, preferencias y reservas

-- =====================================================
-- TABLA: users
-- Descripción: Usuarios de la aplicación
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    phone VARCHAR(30),
    profile_image_url VARCHAR(255),
    travel_preferences VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: user_preferences
-- Descripción: Preferencias estructuradas de cada usuario
-- =====================================================
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    preferred_category VARCHAR(100),
    max_price DECIMAL(10, 2),
    preferred_destination VARCHAR(100),
    activity_duration VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: activities
-- Descripción: Catalogo de actividades disponibles
-- Campos según consigna:
--   - destino: Ciudad o lugar donde se realiza la actividad
--   - categoría: Tipo de actividad (adventure, cultural, etc.)
--   - fecha: Almacenada en activity_availabilities
--   - precio: Costo de la actividad
--   - cupos: available_slots
-- =====================================================
CREATE TABLE IF NOT EXISTS activities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    destination VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL,
    duration VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    available_slots INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_destination (destination),
    INDEX idx_price (price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: activity_availabilities
-- Descripción: Horarios y disponibilidad de actividades
-- =====================================================
CREATE TABLE IF NOT EXISTS activity_availabilities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    activity_id BIGINT NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    total_slots INT NOT NULL,
    reserved_slots INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    INDEX idx_activity (activity_id),
    INDEX idx_date (date),
    UNIQUE KEY unique_availability (activity_id, date, time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: reservations
-- Descripción: Reservas de actividades realizadas por usuarios
-- =====================================================
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    activity_id BIGINT NOT NULL,
    availability_id BIGINT,
    participants INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    FOREIGN KEY (availability_id) REFERENCES activity_availabilities(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_activity (activity_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: otp_entries
-- Descripción: Almacenamiento de códigos OTP temporales
-- =====================================================
CREATE TABLE IF NOT EXISTS otp_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    hashed_otp VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =====================================================

-- Index para búsquedas rápidas de usuarios por email
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Index para búsquedas de reservas por usuario y estado
CREATE INDEX IF NOT EXISTS idx_reservations_user_status ON reservations(user_id, status);

-- Index para búsquedas de actividades por destino y categoría
CREATE INDEX IF NOT EXISTS idx_activities_destination_category ON activities(destination, category);

-- =====================================================
-- DATOS DE PRUEBA (opcional)
-- =====================================================

-- Actividades de ejemplo
INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url)
VALUES 
('Senderismo en Bariloche', 'Excursión de 2 horas por senderos de montaña', 'Bariloche', 'Adventure', '2 hours', 50.00, 20, 'https://via.placeholder.com/400'),
('Tour Cultural Buenos Aires', 'Recorrido por museos y sitios históricos', 'Buenos Aires', 'Cultural', '4 hours', 35.00, 15, 'https://via.placeholder.com/400'),
('Clase de Yoga', 'Clase matutina de yoga y meditación', 'CABA', 'Wellness', '1.5 hours', 25.00, 30, 'https://via.placeholder.com/400');

-- Disponibilidades de ejemplo
INSERT INTO activity_availabilities (activity_id, date, time, total_slots, reserved_slots)
VALUES 
(1, '2025-04-15', '09:00:00', 20, 5),
(1, '2025-04-16', '14:00:00', 20, 3),
(2, '2025-04-17', '10:00:00', 15, 2),
(3, '2025-04-18', '08:00:00', 30, 10);
