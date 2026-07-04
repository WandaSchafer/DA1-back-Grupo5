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
IINSERT INTO activities (
    name, description, destination, category, duration, price, available_slots, image_url, 
    guide_name, meeting_point_address, meeting_point_lat, meeting_point_lng
) VALUES 
('City Tour Buenos Aires', 'Recorrido por los puntos icónicos de la ciudad', 'Buenos Aires, Argentina', 'Cultura', '4h', 30.00, 15, 'https://images.unsplash.com/photo-1514924013411-cbf25faa35bb', 'Juan Pérez', 'Av. Corrientes 1234', -34.6037, -58.3816),
('Excursión Tigre Delta', 'Paseo en lancha por el delta del Tigre', 'Tigre, Argentina', 'Naturaleza', '5h', 50.00, 10, 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e', 'Ana López', 'Estación Fluvial Tigre', -34.4233, -58.5771),
('Tour Gastronómico', 'Degustación de comidas típicas locales', 'Buenos Aires, Argentina', 'Gastronomía', '3h', 60.00, 8, 'https://images.unsplash.com/photo-1504674900247-0877df9cc836', 'Carlos Ruiz', 'Plaza de Mayo S/N', -34.6083, -58.3712),
('Caminata en Bariloche', 'Senderismo por paisajes patagónicos', 'Bariloche, Argentina', 'Aventura', '6h', 80.00, 12, 'https://images.unsplash.com/photo-1501785888041-af3ef285b470', 'Lucía Mestre', 'Centro Cívico Bariloche', -41.1335, -71.3103),
('Tour del Vino Mendoza', 'Visita a bodegas con degustación', 'Mendoza, Argentina', 'Gastronomía', '5h', 90.00, 10, 'https://images.unsplash.com/photo-1510626176961-4b37d0b6c3c3', 'Martín Vid', 'Acceso Este 100', -32.8895, -68.8458),
('Excursión Glaciar Perito Moreno', 'Visita guiada al glaciar', 'El Calafate, Argentina', 'Naturaleza', '8h', 150.00, 6, 'https://images.unsplash.com/photo-1519681393784-d120267933ba', 'Elena Hielo', 'Av. del Libertador 500', -50.3380, -72.2854),
('Free Walking Tour Córdoba', 'Tour a pie gratuito por la ciudad', 'Córdoba, Argentina', 'Free Tour', '2h', 0.00, 20, 'https://images.unsplash.com/photo-1526778548025-fa2f459cd5c1', 'Sofía Guía', 'Plaza San Martín', -31.4167, -64.1833),
('Tour Nocturno Palermo', 'Recorrido nocturno por bares y cultura', 'Buenos Aires, Argentina', 'Ocio', '3h', 25.00, 15, 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30', 'Julián Bar', 'Plaza Serrano', -34.5880, -58.4286),
('Clases de Tango', 'Aprende tango con instructores locales', 'Buenos Aires, Argentina', 'Cultura', '2h', 40.00, 10, 'https://images.unsplash.com/photo-1521336575822-6da63fb45455', 'Roberto Pasos', 'San Telmo Market', -34.6214, -58.3736),
('Kayak en Lago Nahuel Huapi', 'Aventura acuática en kayak', 'Bariloche, Argentina', 'Aventura', '4h', 70.00, 8, 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e', 'Pedro Remo', 'Playa Bonita', -41.1070, -71.3780),
('Tour Arquitectónico Rosario', 'Descubre la arquitectura urbana', 'Rosario, Argentina', 'Cultura', '3h', 20.00, 12, 'https://images.unsplash.com/photo-1501594907352-04cda38ebc29', 'Valeria Arc', 'Monumento a la Bandera', -32.9520, -60.6393),
('Safari Fotográfico Iguazú', 'Captura la fauna local', 'Iguazú, Argentina', 'Naturaleza', '5h', 85.00, 10, 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee', 'Diego Selva', 'Entrada Parque Nacional', -25.6953, -54.4367),
('Excursión Cataratas Iguazú', 'Visita guiada a las cataratas', 'Iguazú, Argentina', 'Naturaleza', '8h', 140.00, 7, 'https://images.unsplash.com/photo-1502082553048-f009c37129b9', 'Silvia Agua', 'Centro de Visitantes', -25.6850, -54.4440),
('Tour Histórico Córdoba', 'Recorrido por sitios históricos', 'Córdoba, Argentina', 'Cultura', '3h', 25.00, 15, 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429', 'Andrés Historia', 'Cabildo de Córdoba', -31.4172, -64.1848),
('Clases de Cocina Criolla', 'Aprende recetas tradicionales', 'Buenos Aires, Argentina', 'Gastronomía', '3h', 55.00, 10, 'https://images.unsplash.com/photo-1498837167922-ddd27525d352', 'Marta Sabor', 'Palermo Soho', -34.5820, -58.4230),
('Cabalgata en la Pampa', 'Paseo a caballo tradicional', 'La Pampa, Argentina', 'Aventura', '4h', 65.00, 8, 'https://images.unsplash.com/photo-1504198453319-5ce911bafcde', 'Juan Campo', 'Estancia La Pampa', -36.6167, -64.2833),
('Excursión Salinas Grandes', 'Visita a paisajes salinos', 'Jujuy, Argentina', 'Naturaleza', '8h', 110.00, 6, 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429', 'Tito Sal', 'Purmamarca', -23.7500, -65.3833);

-- Disponibilidades de ejemplo
INSERT INTO activity_availabilities (activity_id, date, time, total_slots, reserved_slots)
VALUES 
(1, '2025-04-15', '09:00:00', 20, 5),
(1, '2025-04-16', '14:00:00', 20, 3),
(2, '2025-04-17', '10:00:00', 15, 2),
(3, '2025-04-18', '08:00:00', 30, 10);
