# Asignación de guías a las actividades

Este script asigna un guía responsable a cada una de las actividades existentes en la base de datos.

## SQL

```sql
UPDATE activities
SET guide_name = CASE id
    WHEN 1 THEN 'Martín López'
    WHEN 2 THEN 'Sofía Fernández'
    WHEN 3 THEN 'Diego Ramírez'
    WHEN 4 THEN 'Valentina Gómez'
    WHEN 5 THEN 'Lucas Herrera'
    WHEN 6 THEN 'Camila Torres'
    WHEN 7 THEN 'Nicolás Álvarez'
    WHEN 8 THEN 'Julieta Castro'
    WHEN 9 THEN 'Franco Benítez'
    WHEN 10 THEN 'Florencia Suárez'
    WHEN 11 THEN 'Agustín Medina'
    WHEN 12 THEN 'Carolina Rojas'
    WHEN 13 THEN 'Ignacio Peralta'
    WHEN 14 THEN 'Milagros Acosta'
    WHEN 15 THEN 'Joaquín Cabrera'
    WHEN 16 THEN 'Paula Domínguez'
    WHEN 17 THEN 'Tomás Navarro'
    WHEN 18 THEN 'Antonella Vega'
    WHEN 19 THEN 'Santiago Molina'
    WHEN 20 THEN 'Federico Ibarra'
    WHEN 21 THEN 'Brenda Quiroga'
    WHEN 22 THEN 'Emiliano Sosa'
    WHEN 23 THEN 'Melina Aguirre'
    WHEN 24 THEN 'Gonzalo Farías'
    WHEN 25 THEN 'Lucía Ponce'
    WHEN 26 THEN 'Matías Villalba'
    WHEN 27 THEN 'Victoria Ferrero'
END
WHERE id BETWEEN 1 AND 27;
```

## Resultado esperado

Cada actividad tendrá asignado un guía en la columna `guide_name`, permitiendo mostrar el responsable de la excursión desde el backend y el frontend.