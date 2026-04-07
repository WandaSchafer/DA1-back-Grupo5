# 🧪 PLAN DE TESTING Y CASOS DE PRUEBA

## Casos de Prueba - Flujo Completo

---

## ✅ MÓDULO 1: AUTENTICACIÓN

### Test 1.1: Registrar Usuario Nuevo
```
POST /auth/register
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "username": "juanperez",
  "password": "SecurePass123!"
}

RESPUESTA ESPERADA: 201 CREATED
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "juan.perez@example.com",
  "username": "juanperez"
}
```

### Test 1.2: Registrar con Email duplicado
```
POST /auth/register
{
  "email": "juan.perez@example.com",  // Ya existe
  "username": "otro_usuario",
  "password": "SecurePass123!"
}

RESPUESTA ESPERADA: 400 BAD REQUEST
Error: Email ya registrado
```

### Test 1.3: Login correcto
```
POST /auth/login
Content-Type: application/json

{
  "email": "juan.perez@example.com",
  "password": "SecurePass123!"
}

RESPUESTA ESPERADA: 200 OK
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "juan.perez@example.com",
  "username": "juanperez"
}

GUARDAR EL TOKEN para próximos tests:
TOKEN = "eyJhbGciOiJIUzI1NiJ9..."
```

### Test 1.4: Login con password incorrecto
```
POST /auth/login
{
  "email": "juan.perez@example.com",
  "password": "IncorrectPassword!"
}

RESPUESTA ESPERADA: 401 UNAUTHORIZED
Error: Credenciales inválidas
```

### Test 1.5: Solicitar OTP
```
POST /auth/otp/request
Content-Type: application/json

{
  "email": "juan.perez@example.com"
}

RESPUESTA ESPERADA: 200 OK
{
  "message": "OTP enviado correctamente"
}

NOTA: En ambiente de prueba, el OTP se genera pero no se envía por email
Se aguarda 5 minutos (TTL), luego expira
```

---

## ✅ MÓDULO 2: ACTIVIDADES - CATÁLOGO

### Test 2.1: Listar actividades (sin autenticación)
```
GET /api/v1/activities?page=0&size=10
Authorization: (no requerido)

RESPUESTA ESPERADA: 200 OK
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
    },
    ...
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 5,
  "totalElements": 42
}
```

### Test 2.2: Filtrar por categoría
```
GET /api/v1/activities?page=0&size=10&category=Adventure

RESPUESTA ESPERADA: 200 OK
Solo actividades con category=Adventure
```

### Test 2.3: Filtrar por rango de precio
```
GET /api/v1/activities?minPrice=30&maxPrice=80&page=0&size=10

RESPUESTA ESPERADA: 200 OK
Actividades entre $30 y $80
```

### Test 2.4: Filtrar por destino
```
GET /api/v1/activities?destination=Bariloche&page=0&size=10

RESPUESTA ESPERADA: 200 OK
Solo actividades en Bariloche
```

### Test 2.5: Búsqueda por nombre
```
GET /api/v1/activities?search=Senderismo&page=0&size=10

RESPUESTA ESPERADA: 200 OK
Actividades con "Senderismo" en el nombre
```

### Test 2.6: Filtros combinados
```
GET /api/v1/activities?category=Adventure&destination=Bariloche&minPrice=40&maxPrice=100&page=0&size=10

RESPUESTA ESPERADA: 200 OK
Actividades que cumplen TODOS los filtros
```

### Test 2.7: Ver detalles de actividad
```
GET /api/v1/activities/1

RESPUESTA ESPERADA: 200 OK
{
  "id": 1,
  "name": "Senderismo en Bariloche",
  "description": "Excursión de 2 horas por senderos de montaña",
  "destination": "Bariloche",
  "category": "Adventure",
  "duration": "2 hours",
  "price": 50.00,
  "availableSlots": 15,
  "imageUrl": "https://..."
}
```

### Test 2.8: Ver disponibilidad de actividad
```
GET /api/v1/activities/1/availability

RESPUESTA ESPERADA: 200 OK
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

### Test 2.9: Actividad no existe
```
GET /api/v1/activities/99999

RESPUESTA ESPERADA: 404 NOT FOUND
Error: Actividad no encontrada
```

---

## ✅ MÓDULO 3: ACTIVIDADES - RECOMENDACIONES

### Test 3.1: Recomendaciones sin preferencias (primer acceso)
```
GET /api/v1/activities/recommended
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 200 OK
Lista de TODAS las actividades (sin preferencias defin ninguna)
```

### Test 3.2: Recomendaciones con preferencias guardadas
```
1. Primero, actualizar perfil con preferencias (ver Test 4.2)
2. Luego, consultar recomendaciones:

GET /api/v1/activities/recommended
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 200 OK
[
  {
    // SOLO actividades donde category = preferred_category del usuario
  }
]
```

### Test 3.3: Recomendaciones sin autenticación
```
GET /api/v1/activities/recommended

RESPUESTA ESPERADA: 401 UNAUTHORIZED
Error: Autenticación requerida
```

---

## ✅ MÓDULO 4: PERFIL DE USUARIO

### Test 4.1: Ver mi perfil
```
GET /api/v1/users/me
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 200 OK
{
  "id": 1,
  "username": "juanperez",
  "email": "juan.perez@example.com",
  "phone": null,
  "profileImageUrl": null,
  "travelPreferences": null,
  "preferences": null  // Null si no ha configurado preferencias
}
```

### Test 4.2: Actualizar perfil con preferencias
```
PUT /api/v1/users/me
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "username": "juan.perez.updated",
  "phone": "+54911234567",
  "profileImageUrl": "https://example.com/photo.jpg",
  "travelPreferences": "Adventure, Nature",
  "preferences": {
    "preferredCategory": "Adventure",
    "maxPrice": 150.00,
    "preferredDestination": "Bariloche",
    "activityDuration": "long"
  }
}

RESPUESTA ESPERADA: 200 OK
{
  "id": 1,
  "username": "juan.perez.updated",
  "email": "juan.perez@example.com",
  "phone": "+54911234567",
  "profileImageUrl": "https://example.com/photo.jpg",
  "travelPreferences": "Adventure, Nature",
  "preferences": {
    "id": 5,
    "preferredCategory": "Adventure",
    "maxPrice": 150.00,
    "preferredDestination": "Bariloche",
    "activityDuration": "long"
  }
}
```

### Test 4.3: Actualizar solo algunos campos
```
PUT /api/v1/users/me
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "phone": "+54911111111"
  // No incluir otros campos si no cambian
}

RESPUESTA ESPERADA: 200 OK
Actualiza solo phone, mantiene otros datos
```

### Test 4.4: Acceder sin autenticación
```
GET /api/v1/users/me

RESPUESTA ESPERADA: 401 UNAUTHORIZED
Error: Autenticación requerida
```

---

## ✅ MÓDULO 5: RESERVAS

### Test 5.1: Crear reserva
```
POST /api/v1/reservations
Authorization: Bearer TOKEN
Content-Type: application/json

{
  "activityId": 1,
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2
}

RESPUESTA ESPERADA: 201 CREATED
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

### Test 5.2: Reservar sin cupos disponibles
```
POST /api/v1/reservations
Authorization: Bearer TOKEN

{
  "activityId": 1,
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 100  // Más cupos de los disponibles
}

RESPUESTA ESPERADA: 400 BAD REQUEST
Error: No hay cupos disponibles
```

### Test 5.3: Reservar la misma actividad dos veces
```
1. Crear primera reserva (Test 5.1) → OK
2. Intentar crear otra reserva igual:

POST /api/v1/reservations
{
  "activityId": 1,
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 1
}

RESPUESTA ESPERADA: 400 BAD REQUEST
Error: Ya tenés una reserva activa para esta actividad en ese horario
```

### Test 5.4: Ver mis reservas
```
GET /api/v1/reservations/me
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 200 OK
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
  },
  // ... otras reservas
]
```

### Test 5.5: Cancelar reserva
```
PATCH /api/v1/reservations/10/cancel
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 200 OK
{
  "id": 10,
  "activityName": "Senderismo en Bariloche",
  "destination": "Bariloche",
  "imageUrl": "https://...",
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2,
  "status": "CANCELLED",  // ← CANCELADA
  "cancellationPolicy": "Cancelación hasta 24h antes",
  "createdAt": "2025-04-07T10:30:00",
  "totalPrice": 100.00
}
```

### Test 5.6: Cancelar reserva ya cancelada
```
PATCH /api/v1/reservations/10/cancel  // Ya fue cancelada en Test 5.5
Authorization: Bearer TOKEN

RESPUESTA ESPERADA: 400 BAD REQUEST
Error: La reserva ya fue cancelada
```

### Test 5.7: Cancelar reserva de otro usuario
```
1. Usuario A crea reserva 10
2. Usuario B intenta cancelar:

PATCH /api/v1/reservations/10/cancel
Authorization: Bearer TOKEN_USUARIO_B

RESPUESTA ESPERADA: 403 FORBIDDEN
Error: No tienes permiso para cancelar esta reserva
```

### Test 5.8: Crear reserva sin autenticación
```
POST /api/v1/reservations

{
  "activityId": 1,
  "date": "2025-04-15",
  "time": "09:00:00",
  "participants": 2
}

RESPUESTA ESPERADA: 401 UNAUTHORIZED
Error: Autenticación requerida
```

---

## 📊 CHECKLIST DE VALIDACIONES

- [ ] Todos los endpoints retornan status HTTP correcto
- [ ] Mensajes de error son descriptivos
- [ ] DTOs tienen nombres en camelCase
- [ ] Fechas en formato ISO (YYYY-MM-DD)
- [ ] Horas en formato 24h (HH:MM:SS)
- [ ] Autenticación se requiere donde corresponde
- [ ] Autenticación se valida correctamente
- [ ] Permisos se comprueban (usuario solo accede sus datos)
- [ ] Validaciones de negocio funcionan (cupos, duplicados, etc.)
- [ ] Transacciones funcionan correctamente
- [ ] OTP expira después de 5 minutos
- [ ] JWT expira después de 24 horas

---

## 🔄 FLUJO COMPLETO DE PRUEBA (Recomendado)

1. **Registrar usuario** (Test 1.1)
2. **Login** (Test 1.3)
3. **Actualizar perfil con preferencias** (Test 4.2)
4. **Listar actividades** (Test 2.1)
5. **Filtrar actividades** (Test 2.3, 2.4, 2.5, 2.6)
6. **Ver detalles de actividad** (Test 2.7)
7. **Ver disponibilidad** (Test 2.8)
8. **Ver recomendaciones** (Test 3.2) → Debería mostrar solo Adventure
9. **Crear reserva** (Test 5.1)
10. **Ver mis reservas** (Test 5.4)
11. **Cancelar reserva** (Test 5.5)

**Tiempo estimado**: 15-30 minutos

---

## 🐛 BUGS CONOCIDOS / CASOS EDGE

- OTP en ambiente de prueba no se envía por email (implementar email service para producción)
- Búsqueda case-insensitive en todas las BD (OK)
- Validación de contraseña: Mínimo 8 caracteres, 1 mayúscula, 1 número (implementar @Password validator si es necesario)

---

## 📝 NOTAS PARA TESTING

- Usar **Insomnia** o **Postman** para tests manuales
- Para tests automatizados: Usar **JUnit 5** + **MockMvc**
- Las fechas de disponibilidad deben ser futuras (actualizar script SQL si es necesario)
- En cada test, guardar el ID de recurso creado para tests posteriores
