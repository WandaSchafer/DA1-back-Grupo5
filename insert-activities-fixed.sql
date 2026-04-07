-- ============================================
-- INSERTAR ACTIVIDADES DE EJEMPLO - VERSIÓN CORREGIDA
-- ============================================

-- Limpiar datos anteriores
DELETE FROM activity_availabilities;
DELETE FROM activities;

-- ============================================
-- ACTIVIDADES DE AVENTURA - BARILOCHE
-- ============================================

INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Trekking Laguna de los Tres Picos',
    'Caminata de dificultad media hacia una de las lagunas más hermosas de Bariloche. Vistas panorámicas de la cordillera y bosques de ñire. Incluye almuerzo en la montaña.',
    'Bariloche',
    'Adventure',
    '8-10 horas',
    4500,
    8,
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=500'
),
(
    'Escalada en Roca - Cerro Campanario',
    'Escalada guiada en roca para principiantes e intermedios. Equipo de seguridad incluido. Vistas de Bariloche y el Lago Nahuel Huapi desde la cumbre.',
    'Bariloche',
    'Adventure',
    '6 horas',
    3800,
    6,
    'https://images.unsplash.com/photo-1551632786-de41efc74d13?w=500'
),
(
    'Kayak en Lago Nahuel Huapi',
    'Navegación en kayak por las aguas tranquilas del lago. Avistamiento de fauna local. Apto para familias. Incluye chaleco salvavidas y toda la equipación.',
    'Bariloche',
    'Adventure',
    '4-5 horas',
    2200,
    12,
    'https://images.unsplash.com/photo-1505142468610-359e7d316be0?w=500'
),
(
    'Cabalgata en Estancia',
    'Recorrida a caballo por una estancia patagónica tradicional. Paisajes de montañas y ríos. Almuerzo criollo incluido. Para todos los niveles.',
    'Bariloche',
    'Adventure',
    '7 horas',
    3200,
    10,
    'https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?w=500'
);

-- ============================================
-- ACTIVIDADES CULTURALES - BUENOS AIRES
-- ============================================

INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Tour Histórico por San Telmo y La Boca',
    'Recorrida guiada por los barrios más históricos de Buenos Aires. Visita a museos, ver la Casa Rosada, Caminito. Narración histórica de la ciudad.',
    'Buenos Aires',
    'Culture',
    '6 horas',
    2800,
    15,
    'https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=500'
),
(
    'Clase de Tango + Cena Show',
    'Clase práctica de tango con maestro profesional seguida de cena y espectáculo en vivo. Incluye cena con bebidas.',
    'Buenos Aires',
    'Culture',
    '5 horas',
    5500,
    20,
    'https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?w=500'
),
(
    'Museo MALBA + Arte Contemporáneo',
    'Tour guiado por el Museo de Arte Latinoamericano. Obras maestras de Frida Kahlo, Diego Rivera y artistas contemporáneos.',
    'Buenos Aires',
    'Culture',
    '3-4 horas',
    1900,
    10,
    'https://images.unsplash.com/photo-1578321272176-f31a0c6d8f3a?w=500'
),
(
    'Visita Cementerio de la Recoleta',
    'Tour histórico y artístico por el emblemático cementerio. Tumbas famosas, mausoleos arquitectónicos y la tumba de Eva Perón.',
    'Buenos Aires',
    'Culture',
    '2.5 horas',
    1200,
    12,
    'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=500'
);

-- ============================================
-- ACTIVIDADES GASTRONÓMICAS - MENDOZA
-- ============================================

INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Tour de Bodegas + Degustación de Vinos',
    'Visita a 2 bodegas tradicionales con degustación de vinos de alta gama. Aprende sobre el proceso de vinificación. Incluye almuerzo.',
    'Mendoza',
    'Food',
    '7-8 horas',
    4200,
    8,
    'https://images.unsplash.com/photo-1510812431401-41d2cab2707d?w=500'
),
(
    'Cocina Mendocina - Clase en Casa Privada',
    'Clase de cocina regional con chef profesional. Prepara empanadas, locro y postres tradicionales. Degustación final con vino local.',
    'Mendoza',
    'Food',
    '5 horas',
    3500,
    6,
    'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500'
),
(
    'Picnic Gourmet en Viña',
    'Picnic de lujo en medio de viñedos con vinos lokales, quesos artesanales y productos regionales. Atardecer inolvidable.',
    'Mendoza',
    'Food',
    '4 horas',
    3800,
    10,
    'https://images.unsplash.com/photo-1504674900968-e4355056a658?w=500'
),
(
    'Ruta del Vino Experimental',
    'Visita a bodegas boutique y vinícolas pequeñas. Degustación de vinos naturales y Orange wines. Taller de enología.',
    'Mendoza',
    'Food',
    '6 horas',
    4500,
    7,
    'https://images.unsplash.com/photo-1608522479922-ef06bc08a5c0?w=500'
);

-- ============================================
-- ACTIVIDADES RELAJACIÓN/BIENESTAR - SALTA
-- ============================================

INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Retiro de Yoga en Montaña',
    'Sesiones de yoga al aire libre en medio de la naturaleza. Meditación, pranayama y asanas. Incluye almuerzo vegano.',
    'Salta',
    'Wellness',
    '6 horas',
    2500,
    12,
    'https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=500'
),
(
    'Spa Termal Ancestral',
    'Baños termales naturales + masaje relajante + sauna. Tratamientos con productos locales. Ambiente zen.',
    'Salta',
    'Wellness',
    '4 horas',
    2200,
    10,
    'https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=500'
),
(
    'Meditación al Atardecer - Cerro San Bernardo',
    'Sesión de meditación guiada mientras contemplas el atardecer sobre la ciudad de Salta. Incluye té ceremonial.',
    'Salta',
    'Wellness',
    '2.5 horas',
    950,
    15,
    'https://images.unsplash.com/photo-1588286840104-8957b019727f?w=500'
);

-- ============================================
-- ACTIVIDADES DE NATURALEZA - PUERTO IGUAZÚ
-- ============================================

INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Cataratas del Iguazú - Pasarela Superior e Inferior',
    'Tour completo por Parque Nacional Iguazú. Vistas panorámicas desde diferentes perspectivas. Guía naturalista incluido.',
    'Puerto Iguazú',
    'Nature',
    '6-7 horas',
    3200,
    20,
    'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=500'
),
(
    'Safari en Selva Misionera',
    'Excursión en 4x4 por la selva. Avistamiento de fauna (monos, tucanes, coatíes). Zona de reserva Ibera.',
    'Puerto Iguazú',
    'Nature',
    '8 horas',
    4000,
    8,
    'https://images.unsplash.com/photo-1470114716159-e389f8712fda?w=500'
),
(
    'Aventura Extrema - Saltando en Rappel',
    'Descenso en rappel de 30 metros frente a las cataratas. Para aventureros. Equipo de seguridad profesional.',
    'Puerto Iguazú',
    'Adventure',
    '4 horas',
    3800,
    5,
    'https://images.unsplash.com/photo-1501564148684-f5ae9b3c4fb9?w=500'
),
(
    'Catamarán Nocturno - Avistamiento de Estrellas',
    'Navegación nocturna por el río Paraná. Observación de constelaciones. Cena ligera incluida.',
    'Puerto Iguazú',
    'Nature',
    '4 horas',
    2600,
    12,
    'https://images.unsplash.com/photo-1419242902214-272b3f66ee7a?w=500'
);

-- ============================================
-- DISPONIBILIDADES - PROXIMAS 2 SEMANAS
-- ============================================

-- Actividades para Bariloche (del 8 al 22 de abril)
INSERT INTO activity_availabilities (activity_id, date, time, total_slots, reserved_slots) VALUES
(1, '2026-04-08', '09:00:00', 8, 0),
(1, '2026-04-09', '09:00:00', 8, 0),
(1, '2026-04-10', '09:00:00', 7, 0),
(1, '2026-04-11', '09:00:00', 8, 0),
(1, '2026-04-12', '09:00:00', 5, 0),
(1, '2026-04-15', '09:00:00', 8, 0),
(1, '2026-04-16', '09:00:00', 8, 0),
(1, '2026-04-17', '09:00:00', 6, 0),
(1, '2026-04-18', '09:00:00', 8, 0),

(2, '2026-04-08', '10:00:00', 6, 0),
(2, '2026-04-10', '10:00:00', 4, 0),
(2, '2026-04-12', '10:00:00', 6, 0),
(2, '2026-04-14', '10:00:00', 5, 0),
(2, '2026-04-16', '10:00:00', 6, 0),
(2, '2026-04-18', '10:00:00', 3, 0),
(2, '2026-04-20', '10:00:00', 6, 0),

(3, '2026-04-09', '08:00:00', 12, 0),
(3, '2026-04-11', '08:00:00', 10, 0),
(3, '2026-04-13', '08:00:00', 12, 0),
(3, '2026-04-15', '08:00:00', 8, 0),
(3, '2026-04-17', '08:00:00', 12, 0),
(3, '2026-04-19', '08:00:00', 11, 0),

(4, '2026-04-08', '08:30:00', 10, 0),
(4, '2026-04-11', '08:30:00', 9, 0),
(4, '2026-04-14', '08:30:00', 10, 0),
(4, '2026-04-17', '08:30:00', 7, 0),
(4, '2026-04-20', '08:30:00', 10, 0),

-- Actividades para Buenos Aires (del 9 al 20 de abril)
(5, '2026-04-09', '10:00:00', 15, 0),
(5, '2026-04-10', '10:00:00', 14, 0),
(5, '2026-04-12', '10:00:00', 15, 0),
(5, '2026-04-14', '10:00:00', 13, 0),
(5, '2026-04-16', '10:00:00', 15, 0),
(5, '2026-04-18', '10:00:00', 12, 0),
(5, '2026-04-20', '10:00:00', 15, 0),

(6, '2026-04-09', '20:00:00', 20, 0),
(6, '2026-04-11', '20:00:00', 18, 0),
(6, '2026-04-13', '20:00:00', 20, 0),
(6, '2026-04-15', '20:00:00', 15, 0),
(6, '2026-04-17', '20:00:00', 20, 0),
(6, '2026-04-19', '20:00:00', 19, 0),

(7, '2026-04-10', '14:00:00', 10, 0),
(7, '2026-04-12', '14:00:00', 9, 0),
(7, '2026-04-14', '14:00:00', 10, 0),
(7, '2026-04-16', '14:00:00', 8, 0),
(7, '2026-04-18', '14:00:00', 10, 0),
(7, '2026-04-20', '14:00:00', 7, 0),

(8, '2026-04-09', '10:00:00', 12, 0),
(8, '2026-04-11', '10:00:00', 12, 0),
(8, '2026-04-13', '10:00:00', 10, 0),
(8, '2026-04-15', '10:00:00', 12, 0),
(8, '2026-04-17', '10:00:00', 11, 0),
(8, '2026-04-19', '10:00:00', 12, 0),

-- Actividades para Mendoza (del 8 al 22 de abril)
(9, '2026-04-08', '09:00:00', 8, 0),
(9, '2026-04-10', '09:00:00', 7, 0),
(9, '2026-04-12', '09:00:00', 8, 0),
(9, '2026-04-14', '09:00:00', 6, 0),
(9, '2026-04-16', '09:00:00', 8, 0),
(9, '2026-04-18', '09:00:00', 5, 0),
(9, '2026-04-20', '09:00:00', 8, 0),

(10, '2026-04-09', '10:00:00', 6, 0),
(10, '2026-04-11', '10:00:00', 5, 0),
(10, '2026-04-13', '10:00:00', 6, 0),
(10, '2026-04-15', '10:00:00', 4, 0),
(10, '2026-04-17', '10:00:00', 6, 0),
(10, '2026-04-19', '10:00:00', 3, 0),

(11, '2026-04-08', '10:00:00', 10, 0),
(11, '2026-04-11', '10:00:00', 9, 0),
(11, '2026-04-14', '10:00:00', 10, 0),
(11, '2026-04-17', '10:00:00', 8, 0),
(11, '2026-04-20', '10:00:00', 10, 0),

(12, '2026-04-09', '09:00:00', 7, 0),
(12, '2026-04-12', '09:00:00', 6, 0),
(12, '2026-04-15', '09:00:00', 7, 0),
(12, '2026-04-18', '09:00:00', 5, 0),
(12, '2026-04-21', '09:00:00', 7, 0),

-- Actividades para Salta (del 9 al 21 de abril)
(13, '2026-04-09', '08:00:00', 12, 0),
(13, '2026-04-11', '08:00:00', 11, 0),
(13, '2026-04-13', '08:00:00', 12, 0),
(13, '2026-04-15', '08:00:00', 10, 0),
(13, '2026-04-17', '08:00:00', 12, 0),
(13, '2026-04-19', '08:00:00', 9, 0),

(14, '2026-04-10', '14:00:00', 10, 0),
(14, '2026-04-12', '14:00:00', 9, 0),
(14, '2026-04-14', '14:00:00', 10, 0),
(14, '2026-04-16', '14:00:00', 8, 0),
(14, '2026-04-18', '14:00:00', 10, 0),
(14, '2026-04-20', '14:00:00', 7, 0),

(15, '2026-04-09', '17:30:00', 15, 0),
(15, '2026-04-10', '17:30:00', 15, 0),
(15, '2026-04-12', '17:30:00', 14, 0),
(15, '2026-04-14', '17:30:00', 15, 0),
(15, '2026-04-16', '17:30:00', 13, 0),
(15, '2026-04-18', '17:30:00', 15, 0),
(15, '2026-04-20', '17:30:00', 12, 0),

-- Actividades para Puerto Iguazú (del 8 al 22 de abril)
(16, '2026-04-08', '08:00:00', 20, 0),
(16, '2026-04-10', '08:00:00', 18, 0),
(16, '2026-04-12', '08:00:00', 20, 0),
(16, '2026-04-14', '08:00:00', 16, 0),
(16, '2026-04-16', '08:00:00', 20, 0),
(16, '2026-04-18', '08:00:00', 19, 0),
(16, '2026-04-20', '08:00:00', 20, 0),

(17, '2026-04-09', '08:00:00', 8, 0),
(17, '2026-04-11', '08:00:00', 7, 0),
(17, '2026-04-13', '08:00:00', 8, 0),
(17, '2026-04-15', '08:00:00', 5, 0),
(17, '2026-04-17', '08:00:00', 8, 0),
(17, '2026-04-19', '08:00:00', 6, 0),

(18, '2026-04-10', '09:00:00', 5, 0),
(18, '2026-04-12', '09:00:00', 4, 0),
(18, '2026-04-14', '09:00:00', 5, 0),
(18, '2026-04-16', '09:00:00', 3, 0),
(18, '2026-04-18', '09:00:00', 5, 0),
(18, '2026-04-20', '09:00:00', 2, 0),

(19, '2026-04-08', '21:00:00', 12, 0),
(19, '2026-04-11', '21:00:00', 11, 0),
(19, '2026-04-14', '21:00:00', 12, 0),
(19, '2026-04-17', '21:00:00', 10, 0),
(19, '2026-04-20', '21:00:00', 12, 0);

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT 'ACTIVIDADES CARGADAS' as Status, COUNT(*) as total FROM activities;
SELECT 'DISPONIBILIDADES CARGADAS' as Status, COUNT(*) as total FROM activity_availabilities;
SELECT category, COUNT(*) as cantidad FROM activities GROUP BY category;
SELECT destination, COUNT(*) as cantidad FROM activities GROUP BY destination;
