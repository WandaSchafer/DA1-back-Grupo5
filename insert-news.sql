-- Tabla de Noticias/Beneficios
-- Relaciona noticias con actividades de la app

CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    image_url VARCHAR(500),
    activity_id BIGINT,
    link_type VARCHAR(20) DEFAULT 'ACTIVITY',
    link_url VARCHAR(500),
    FOREIGN KEY (activity_id) REFERENCES activities(id) ON DELETE SET NULL
);

-- Insertar datos de ejemplo
INSERT INTO news (title, description, image_url, activity_id, link_type, link_url) VALUES
('¡Nueva Excursión a la Montaña!', 'Descubre nuestros nuevos senderos en la montaña. Una experiencia única para los amantes de la naturaleza.', 'https://example.com/images/mountain.jpg', 1, 'ACTIVITY', NULL),
('Descuento del 20% en Tours Guiados', 'Reserva ahora y obtén un 20% de descuento en todos nuestros tours guiados durante este mes.', 'https://example.com/images/discount.jpg', 2, 'ACTIVITY', NULL),
('Temporada de Avistamiento de Ballenas', 'No te pierdas la oportunidad de ver ballenas en su hábitat natural. Cupos limitados.', 'https://example.com/images/whale.jpg', 3, 'ACTIVITY', NULL),
('Pack Familiar: Aventura en la Selva', 'Planifica tus próximas vacaciones con nuestra oferta especial para familias.', 'https://example.com/images/jungle.jpg', 4, 'ACTIVITY', NULL),
('Clase de Salsa Gratis', 'Aprende a bailar salsa con nuestros instructores profesionales. Primera clase sin costo.', 'https://example.com/images/dance.jpg', 5, 'ACTIVITY', NULL);