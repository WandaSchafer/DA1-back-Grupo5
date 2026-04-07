# IMPLEMENTACIÓN BACKEND - ACTIVIDADES Y RESERVAS
## Documentación Completa de Endpoints y Funcionalidades

---

## 📋 RESUMEN DE CAMBIOS

### ✅ Completado

1. **Tabla UserPreferences**: Nueva tabla para almacenar preferencias estructuradas del usuario
2. **OTP en Base de Datos**: Migración de almacenamiento en memoria a tabla MySQL
3. **Filtros en Actividades**: GET /api/v1/activities con parámetros de búsqueda avanzada
4. **Recomendaciones mejoradas**: Endpoint /recommended usa usuario autenticado y nuevas preferencias
5. **DTOs mejorados**: UserProfileResponse ahora incluye preferencias estructuradas
6. **Autenticación**: Todos los servicios usan SecurityContext para obtener usuario autenticado
7. **Seguridad**: Validación de permisos en cancelación de reservas

---

## 🗄️ ESQUEMA DE BASE DE DATOS

### Nuevas Tablas/Cambios:

#### `user_preferences`
```sql
CREATE TABLE user_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    preferred_category VARCHAR(100),
    max_price DECIMAL(10, 2),
    preferred_destination VARCHAR(100),
    activity_duration VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

#### `otp_entries` (Migrada de memoria)
```sql
CREATE TABLE otp_entries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    hashed_otp VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Tablas Existentes (sin cambios):
- `users` - Usuarios con datos básicos y rol
- `activities` - Catálogo de actividades con destino, categoría, precio, cupos
- `activity_availabilities` - Fechas, horarios y disponibilidad por actividad
- `reservations` - Reservas de usuarios para actividades

---

## 🔌 ENDPOINTS API

### 📝 AUTENTICACIÓN

#### `POST /auth/register`
Registrar nuevo usuario
```json
{
  "email": "usuario@example.com",
  "username": "usuario123",
  "password": "Password123!"
}
```

#### `POST /auth/login`
Iniciar sesión
```json
{
  "email": "usuario@example.com",
  "password": "Password123!"
}
```
Retorna JWT token

#### `POST /auth/otp/request`
Solicitar OTP (almacenado en BD ahora)
```json
{
  "email": "usuario@example.com"
}
```

#### `POST /auth/otp/verify`
Verificar OTP y obtener token
```json
{
  "email": "usuario@example.com",
  "otp": "123456"
}
```

---

### 🎯 ACTIVIDADES (Catálogo)

#### `GET /api/v1/activities` - Lista Paginada CON FILTROS
**Parámetros Query:**
- `page=0` - Página (default: 0)
- `size=10` - Items por página (default: 10)
- `category=Adventure` - Filtrar por categoría
- `destination=Bariloche` - Filtrar por destino
- `minPrice=20` - Precio mínimo
- `maxPrice=100` - Precio máximo
- `search=Senderismo` - Búsqueda por nombre

**Ejemplo completo:**
```
GET /api/v1/activities?page=0&size=10&category=Adventure&destination=Bariloche&minPrice=30&maxPrice=80
```

**Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Senderismo en Bariloche",
      "destination": "Bariloche",
      "category": "Adventure",
      "duration": "2 hours",
      "price": 50.00,
      "availableSlots": 15,
      "imageUrl": "https://..."
    }
  ],
  "pageable": { "pageNumber": 0, "pageSize": 10 },
  "totalPages": 5,
  "totalElements": 42
}
```

#### `GET /api/v1/activities/recommended` - Actividades Recomendadas (Requiere Autenticación)
Devuelve actividades basadas en:
1. `preferred_category` de user_preferences (si existe)
2. `travelPreferences` del usuario (retrocompatibilidad)
3. Todas las actividades si no hay preferencias

**Header requerido:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Senderismo en Bariloche",
    "destination": "Bariloche",
    "category": "Adventure",
    "duration": "2 hours",
    "price": 50.00,
    "availableSlots": 15,
    "imageUrl": "https://..."
  }
]
```

#### `GET /api/v1/activities/{id}` - Detalle de Actividad
```
GET /api/v1/activities/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Senderismo en Bariloche",
  "description": "Excursión de 2 horas...",
  "destination": "Bariloche",
  "category": "Adventure",
  "duration": "2 hours",
  "price": 50.00,
  "availableSlots": 15,
  "imageUrl": "https://..."
}
```

#### `GET /api/v1/activities/{id}/availability` - Horarios Disponibles
```
GET /api/v1/activities/1/availability
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "date": "2025-04-15",
    "time": "09:00:00",
    "availableSlots": 15,
    "totalSlots": 20
  },
  {
    "id": 2,
    "date": "2025-04-15",
    "time": "14:00:00",
    "availableSlots": 12,
    "totalSlots": 20
  }
]
```

---

### 👤 PERFIL DE USUARIO (Requiere Autenticación)

#### `GET /api/v1/users/me` - Mi Perfil
```
GET /api/v1/users/me
Authorization: Bearer <JWT_TOKEN>
```

**Respuesta:**
```json
{
  "id": 1,
  "username": "usuario123",
  "email": "usuario@example.com",
  "phone": "+54911234567",
  "profileImageUrl": "https://...",
  "travelPreferences": "Adventure, Cultural",
  "preferences": {
    "id": 5,
    "preferredCategory": "Adventure",
    "maxPrice": 100.00,
    "preferredDestination": "Bariloche",
    "activityDuration": "long"
  }
}
```

#### `PUT /api/v1/users/me` - Actualizar Perfil
```
PUT /api/v1/users/me
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Body:**
```json
{
  "username": "nuevoUsername",
  "phone": "+54911234567",
  "profileImageUrl": "https://...",
  "travelPreferences": "Adventure, Nature",
  "preferences": {
    "preferredCategory": "Adventure",
    "maxPrice": 150.00,
    "preferredDestination": "Bariloche",
    "activityDuration": "long"
  }
}
```

**Respuesta:** Mismo formato que GET /me

---

### 🎫 RESERVAS (Requiere Autenticación)

#### `POST /api/v1/reservations` - Crear Reserva
```
POST /api/v1/reservations
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Body:**
```json
{
  "activityId": 1,
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2
}
```

**Respuesta:**
```json
{
  "id": 10,
  "activityName": "Senderismo en Bariloche",
  "destination": "Bariloche",
  "imageUrl": "https://...",
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2,
  "status": "CONFIRMED",
  "cancellationPolicy": "Cancelación hasta 24h antes",
  "createdAt": "2025-04-07T10:30:00",
  "totalPrice": 100.00
}
```

#### `GET /api/v1/reservations/me` - Mis Reservas
```
GET /api/v1/reservations/me
Authorization: Bearer <JWT_TOKEN>
```

**Respuesta:**
```json
[
  {
    "id": 10,
    "activityName": "Senderismo en Bariloche",
    "destination": "Bariloche",
    "imageUrl": "https://...",
    "date": "2025-04-15",
    "time": "09:00:00",
    "participants": 2,
    "status": "CONFIRMED",
    "cancellationPolicy": "Cancelación hasta 24h antes",
    "createdAt": "2025-04-07T10:30:00",
    "totalPrice": 100.00
  }
]
```

#### `PATCH /api/v1/reservations/{id}/cancel` - Cancelar Reserva
```
PATCH /api/v1/reservations/10/cancel
Authorization: Bearer <JWT_TOKEN>
```

**Respuesta:**
```json
{
  "id": 10,
  "activityName": "Senderismo en Bariloche",
  "destination": "Bariloche",
  "imageUrl": "https://...",
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2,
  "status": "CANCELLED",
  "cancellationPolicy": "Cancelación hasta 24h antes",
  "createdAt": "2025-04-07T10:30:00",
  "totalPrice": 100.00
}
```

---

## 📝 NOTAS IMPORTANTES

### DTOs con nombres en camelCase (compatibilidad Android)
Todos los DTOs usan **camelCase** en la respuesta JSON:
- `availableSlots` (no `available_slots`)
- `imageUrl` (no `image_url`)
- `profileImageUrl` (no `profile_image_url`)

### Autenticación
- Endpoints de actividades públicas: SIN autenticación
- Endpoints de perfil, reservas, recomendaciones: CON autenticación
- El usuario se obtiene automáticamente del JWT token

### Preferencias de Usuario
Se almacenan de dos formas (por retrocompatibilidad):
1. **Nueva**: `user_preferences` table (estructura)
2. **Antigua**: `travel_preferences` en users (string)

El sistema prioriza las nuevas preferencias si existen.

### Validaciones
- No se puede reservar la misma actividad en el mismo horario dos veces
- No se puede cancelar reservas ya canceladas o finalizadas
- Se valida disponibilidad de cupos antes de confirmar reserva
- El usuario solo puede ver/cancelar sus propias reservas

---

## 🔐 GUÍA DE SEGURIDAD

1. **JWT**: Token expira en 24 horas (configurable en application.properties)
2. **OTP**: Código expira en 5 minutos (configurable en application.properties)
3. **Contraseñas**: BCrypt encoding, nunca se guardan en texto plano
4. **CORS**: Habilitado para desarrollo (ajustar en producción)
5. **Permisos**: Validación por usuario en reservas

---

## 🚀 PRÓXIMOS PASOS

1. Ejecutar script SQL en la BD (database-schema.sql)
2. Configurar variables en `application.properties` (BD, JWT, OTP)
3. Compilar el proyecto: `mvn clean install`
4. Ejecutar: `java -jar target/auth-backend-0.0.1-SNAPSHOT.jar`
5. Probar endpoints con Postman/Insomnia

---

## 📞 CONTACTO Y SOPORTE

Documentación generada para compatibilidad con:
- **Frontend Android**: https://github.com/SanTegli/DA1-android-Grupo5
- **Backend**: https://github.com/WandaSchafer/DA1-back-Grupo5
