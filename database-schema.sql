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
    check_in_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    FOREIGN KEY (availability_id) REFERENCES activity_availabilities(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_activity (activity_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
s
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
-- TABLA: ratings
-- Descripción: Calificaciones de actividades y guías por usuarios
-- Validaciones: Solo dentro de 48 horas después de completada la actividad
-- =====================================================
CREATE TABLE IF NOT EXISTS ratings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    activity_id BIGINT NOT NULL,
    activity_score INT NOT NULL CHECK (activity_score >= 1 AND activity_score <= 5),
    guide_score INT NOT NULL CHECK (guide_score >= 1 AND guide_score <= 5),
    comment VARCHAR(300),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_activity_rating (user_id, activity_id),
    INDEX idx_activity (activity_id),
    INDEX idx_user (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =====================================================

-- Index para búsquedas rápidas de usuarios por email
CREATE INDEX idx_users_email ON users(email);

-- Index para búsquedas de reservas por usuario y estado
CREATE INDEX idx_reservations_user_status ON reservations(user_id, status);

-- Index para búsquedas de actividades por destino y categoría
CREATE INDEX idx_activities_destination_category ON activities(destination, category);

-- =====================================================
-- DATOS DE PRUEBA (opcional)
-- =====================================================

-- Actividades de ejemplo


USE DAI;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE ratings;
TRUNCATE TABLE reservations;
TRUNCATE TABLE activity_availabilities;
TRUNCATE TABLE activities;

ALTER TABLE ratings AUTO_INCREMENT = 1;
ALTER TABLE reservations AUTO_INCREMENT = 1;
ALTER TABLE activity_availabilities AUTO_INCREMENT = 1;
ALTER TABLE activities AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

SET @user_id = (SELECT id FROM users WHERE email = 'wandaeschafer@gmail.com' LIMIT 1);

INSERT INTO activities
(name, description, destination, zone, category, duration, price, available_slots, image_url, guide_name,
 meeting_point_address, meeting_point_lat, meeting_point_lng, created_at, updated_at)
VALUES
('City Tour Buenos Aires', 'Recorrido por puntos icónicos de la ciudad.', 'Buenos Aires', 'Microcentro', 'Cultura', '3 horas', 25000, 20, 'https://images.unsplash.com/photo-1589909202802-8f4aadce1849', 'Lucía Fernández', 'Obelisco, Buenos Aires', -34.6037345, -58.3815704, NOW(), NOW()),
('Tour Gastronómico Palermo', 'Experiencia culinaria por Palermo.', 'Buenos Aires', 'Palermo', 'Gastronomía', '3 horas', 40000, 10, 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5', 'Federico López', 'Plaza Serrano, Palermo', -34.588889, -58.430556, NOW(), NOW()),
('Bici en Puerto Madero', 'Recorrido en bicicleta por Puerto Madero.', 'Buenos Aires', 'Puerto Madero', 'Aventura', '2 horas', 22000, 16, 'https://images.unsplash.com/photo-1485965120184-e220f721d03e', 'Valentina Castro', 'Puente de la Mujer', -34.608333, -58.363889, NOW(), NOW()),
('Kayak en Tigre', 'Actividad en el delta del Tigre.', 'Tigre', 'Tigre', 'Naturaleza', '2 horas', 28000, 12, 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429', 'Sofía Ramírez', 'Estación Fluvial Tigre', -34.420590, -58.579656, NOW(), NOW()),
('Trekking en Tandil', 'Caminata por las sierras.', 'Tandil', 'Tandil', 'Aventura', '4 horas', 32000, 15, 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee', 'Martín Gómez', 'Parque Independencia', -37.328489, -59.136102, NOW(), NOW()),
('Excursión de prueba recordatorio', 'Actividad para probar la notificación previa.', 'Buenos Aires', 'Centro', 'Aventura', '2 horas', 15000, 20, 'https://picsum.photos/600/400', 'Juan Pérez', 'Puerto Madero', -34.6037, -58.3816, NOW(), NOW());

INSERT INTO activity_availabilities
(activity_id, date, time, total_slots, reserved_slots, created_at, updated_at)
VALUES
(1, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00', 20, 0, NOW(), NOW()),
(1, DATE_ADD(CURDATE(), INTERVAL 3 DAY), '15:30:00', 20, 0, NOW(), NOW()),
(2, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '19:00:00', 10, 0, NOW(), NOW()),
(3, DATE_ADD(CURDATE(), INTERVAL 4 DAY), '16:00:00', 16, 0, NOW(), NOW()),
(4, DATE_ADD(CURDATE(), INTERVAL 5 DAY), '11:00:00', 12, 0, NOW(), NOW()),
(5, DATE_ADD(CURDATE(), INTERVAL 6 DAY), '09:00:00', 15, 0, NOW(), NOW()),
(6, DATE_ADD(CURDATE(), INTERVAL 1 DAY), '17:30:00', 20, 0, NOW(), NOW()),

-- Pasadas para historial/finalizadas
(1, DATE_SUB(CURDATE(), INTERVAL 3 DAY), '10:00:00', 20, 2, NOW(), NOW()),
(2, DATE_SUB(CURDATE(), INTERVAL 5 DAY), '19:00:00', 10, 2, NOW(), NOW()),

-- Cancelada
(4, DATE_ADD(CURDATE(), INTERVAL 2 DAY), '12:00:00', 12, 1, NOW(), NOW());

INSERT INTO reservations
(user_id, activity_id, availability_id, participants, status, created_at, updated_at, check_in_at)
VALUES
(@user_id, 1, 1, 2, 'CONFIRMED', NOW(), NOW(), NULL),
(@user_id, 2, 3, 1, 'CONFIRMED', NOW(), NOW(), NULL),
(@user_id, 1, 8, 2, 'FINISHED', DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(@user_id, 2, 9, 2, 'FINISHED', DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(@user_id, 4, 10, 1, 'CANCELLED', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL);


SELECT * FROM activities;
SELECT * FROM activity_availabilities;
SELECT * FROM reservations;
SELECT * FROM ratings;

