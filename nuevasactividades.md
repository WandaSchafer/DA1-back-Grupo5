# Nuevas actividades y disponibilidades

Este script agrega ocho nuevas actividades al sistema y crea una disponibilidad para cada una con fechas posteriores al **07/07/2026**.

> **Importante:** Se asume que las nuevas actividades obtendrán automáticamente los IDs **20 al 27**.

## Crear actividades

```sql
INSERT INTO activities (name, description, destination, category, duration, price, available_slots, image_url) VALUES
(
    'Vuelo en Parapente sobre las Sierras',
    'Experimenta un vuelo biplaza acompañado por un instructor certificado. Disfruta de vistas panorámicas de montañas y valles. Incluye equipo completo y fotografías digitales.',
    'Córdoba',
    'Adventure',
    '2 horas',
    6500,
    8,
    'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=500'
),
(
    'Festival de Comida Callejera',
    'Recorrido gastronómico por los mejores food trucks de la ciudad. Degustación de platos regionales y cocina internacional con guía especializado.',
    'Rosario',
    'Food',
    '4 horas',
    2900,
    20,
    'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=500'
),
(
    'Observación Astronómica en la Montaña',
    'Noche de observación del cielo con telescopios profesionales y guía astronómico. Incluye chocolate caliente y material informativo.',
    'San Juan',
    'Nature',
    '3 horas',
    2400,
    15,
    'https://images.unsplash.com/photo-1465101046530-73398c7f28ca?w=500'
),
(
    'Tour Fotográfico Urbano',
    'Recorre los lugares más fotogénicos de la ciudad junto a un fotógrafo profesional. Ideal para aficionados y principiantes.',
    'Buenos Aires',
    'Culture',
    '5 horas',
    3100,
    12,
    'https://images.unsplash.com/photo-1493246507139-91e8fad9978e?w=500'
),
(
    'Circuito de Termas Naturales',
    'Jornada de relajación en aguas termales naturales con acceso a piscinas, spa y zonas de descanso.',
    'Entre Ríos',
    'Wellness',
    '6 horas',
    3700,
    18,
    'https://images.unsplash.com/photo-1519823551278-64ac92734fb1?w=500'
),
(
    'Paseo en Velero al Atardecer',
    'Navegación por la costa con capitán experimentado. Incluye snacks, bebidas y vista privilegiada del atardecer.',
    'Mar del Plata',
    'Adventure',
    '3 horas',
    4800,
    10,
    'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500'
),
(
    'Senderismo entre Cascadas',
    'Caminata guiada por senderos naturales visitando tres cascadas escondidas. Incluye picnic y guía ambiental.',
    'Misiones',
    'Nature',
    '7 horas',
    3900,
    14,
    'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=500'
),
(
    'Clase de Cerámica Artesanal',
    'Aprende técnicas de modelado y torno junto a artesanos locales. Incluye materiales y una pieza para llevar.',
    'Tucumán',
    'Culture',
    '3 horas',
    2100,
    10,
    'https://images.unsplash.com/photo-1515704867308-7e3f52f7c2a4?w=500'
);
```

## Crear disponibilidades

```sql
INSERT INTO activity_availabilities (activity_id, date, time, total_slots, reserved_slots) VALUES
(20, '2026-07-07', '20:00:00', 8, 0),
(21, '2026-07-08', '19:00:00', 20, 0),
(22, '2026-07-09', '21:00:00', 15, 0),
(23, '2026-07-10', '10:00:00', 12, 0),
(24, '2026-07-11', '09:00:00', 18, 0),
(25, '2026-07-12', '18:30:00', 10, 0),
(26, '2026-07-13', '08:30:00', 14, 0),
(27, '2026-07-14', '16:00:00', 10, 0);
```

## Descripción de las nuevas actividades

| ID | Actividad | Destino | Categoría |
|----|-----------|----------|-----------|
| 20 | Vuelo en Parapente sobre las Sierras | Córdoba | Adventure |
| 21 | Festival de Comida Callejera | Rosario | Food |
| 22 | Observación Astronómica en la Montaña | San Juan | Nature |
| 23 | Tour Fotográfico Urbano | Buenos Aires | Culture |
| 24 | Circuito de Termas Naturales | Entre Ríos | Wellness |
| 25 | Paseo en Velero al Atardecer | Mar del Plata | Adventure |
| 26 | Senderismo entre Cascadas | Misiones | Nature |
| 27 | Clase de Cerámica Artesanal | Tucumán | Culture |