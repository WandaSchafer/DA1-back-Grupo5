# 🏗️ ARQUITECTURA Y DECISIONES DE DISEÑO

## Resumen Ejecutivo

Este documento describe la arquitectura del backend implementado para la aplicación de reserva de actividades, con énfasis en las decisiones de diseño y patrones utilizados para asegurar escalabilidad, seguridad y mantenibilidad.

---

## 📐 ARQUITECTURA GENERAL

```
┌─────────────────────────────────────────────────────────────┐
│                    Cliente Android / Web                      │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTP/REST
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   Spring Boot Application                     │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Controllers (REST Endpoints)              │ │
│  │  - ActivityController                                  │ │
│  │  - UserController                                      │ │
│  │  - ReservationController                               │ │
│  │  - AuthController                                      │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ▲                                   │
│                           │                                   │
│  ┌────────────────────────▼──────────────────────────────┐ │
│  │              Services (Business Logic)                │ │
│  │  - ActivityService                                    │ │
│  │  - UserService                                        │ │
│  │  - ReservationService                                 │ │
│  │  - AuthService                                        │ │
│  │  - OtpService                                         │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ▲                                   │
│                           │                                   │
│  ┌────────────────────────▼──────────────────────────────┐ │
│  │          Repositories (Data Access)                   │ │
│  │  - ActivityRepository                                 │ │
│  │  - UserRepository                                     │ │
│  │  - UserPreferenceRepository (NEW)                     │ │
│  │  - ReservationRepository                              │ │
│  │  - OtpEntryRepository (NEW)                           │ │
│  │  - ActivityAvailabilityRepository                     │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ▲                                   │
│                           │ JPA/Hibernate                    │
│  ┌────────────────────────▼──────────────────────────────┐ │
│  │              Domain Entities (JPA)                    │ │
│  │  - User                                               │ │
│  │  - Activity                                           │ │
│  │  - ActivityAvailability                               │ │
│  │  - Reservation                                        │ │
│  │  - UserPreference (NEW)                               │ │
│  │  - OtpEntry (NEW)                                     │ │
│  └────────────────────────────────────────────────────────┘ │
│                           ▲                                   │
│                           │                                   │
│  ┌────────────────────────▼──────────────────────────────┐ │
│  │              Data Transfer Objects (DTOs)             │ │
│  │  - ActivityListItemResponse                           │ │
│  │  - ActivityDetailResponse                             │ │
│  │  - UserProfileResponse                                │ │
│  │  - UserPreferenceResponse (NEW)                       │ │
│  │  - ReservationResponse                                │ │
│  │  - UpdateUserProfileRequest                           │ │
│  │  - UserPreferenceRequest (NEW)                        │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                                │
│  ┌────────────────────────────────────────────────────────┐ │
│  │           Security & Filters                          │ │
│  │  - JwtAuthenticationFilter                            │ │
│  │  - SecurityConfig                                     │ │
│  │  - CustomUserDetailsService                           │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ JDBC/SQL
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                     MySQL Database                            │
│  - users                                                      │
│  - user_preferences                                           │
│  - activities                                                 │
│  - activity_availabilities                                    │
│  - reservations                                               │
│  - otp_entries                                                │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔐 PATRONES DE SEGURIDAD IMPLEMENTADOS

### 1. **JWT Authentication**
- **Token secreto**: Configurado en `application.properties`
- **Expiración**: 24 horas (configurable)
- **Método**: HS256 (HMAC with SHA-256)
- **Use**: Bearer token en header Authorization

```java
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 2. **OTP (One-Time Password)**
- **Antigua implementación**: Almacenada en ConcurrentHashMap (memoria)
- **Nueva implementación**: Tabla `otp_entries` en MySQL
- **Expiración**: 5 minutos
- **Hashing**: BCrypt para código

**Ventajas de migrar a BD:**
- ✅ Persistencia entre reinicios
- ✅ Escalabilidad (múltiples instancias)
- ✅ Auditoría
- ✅ Recovery en caso de fallos

### 3. **Password Management**
- **Hashing**: BCrypt con 10 rondas (por defecto)
- **Contraseña nunca**: Se almacena en texto plano
- **Validación**: Email único, Username único

### 4. **User Context / SecurityContext**
Todos los servicios obtienen el usuario autenticado mediante:

```java
SecurityContext context = SecurityContextHolder.getContext();
Authentication auth = context.getAuthentication();
String email = auth.getName();
User user = userRepository.findByEmail(email).orElse(...);
```

**Ventajas:**
- ✅ No se hardcodea usuario
- ✅ Compatible con múltiples usuarios simultáneos
- ✅ Autorización basada en usuario

### 5. **Autorización por Recurso**
```java
// En ReservationService.cancelReservation()
if (!r.getUser().getId().equals(user.getId())) {
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso");
}
```

---

## 🏢 PATRONES DE DISEÑO

### Patrón: **Repository**
```
Entity ←→ Repository ←→ Database

Ejemplo:
Activity (entidad) → ActivityRepository (interfaz) → MySQL
```

**Ventajas:**
- Abstracción de BD
- Fácil testing (mock repositories)
- Queries reutilizables

### Patrón: **Service Layer**
```
Controller → Service → Repository → DB

ActivityController → ActivityService → ActivityRepository → MySQL
```

**Responsabilidades:**
- Controllers: Validar request, llamar services, serializar respuesta
- Services: Lógica de negocio, transacciones, validaciones
- Repositories: Queries, persistencia

### Patrón: **DTO (Data Transfer Object)**
```java
// NO exponemos entidades directamente
Activity (entity) → ActivityListItemResponse (DTO) → JSON → Cliente
```

**Ventajas:**
- ✅ Campo `password` nunca se serializa
- ✅ Control de qué datos se envían
- ✅ Compatibilidad Android (camelCase)
- ✅ Versionamiento de API

---

## 📊 MODELADO DE DATOS

### Relaciones Entidad-Relación

```
┌─────────────────┐         ┌────────────────┐
│     User        │         │  UserPreference│
├─────────────────┤         ├────────────────┤
│ id (PK)         │───1:1───│ id (PK)        │
│ email (UNIQUE)  │         │ user_id (FK)   │
│ username        │         │ preferred_cat  │
│ password        │         │ max_price      │
│ role            │         │ destination    │
│ travelPref      │         │ duration       │
└────────┬────────┘         └────────────────┘
         │
         │ 1:N
         │
    ┌────▼──────────────┐     ┌──────────────────┐
    │  Reservation       │     │     Activity     │
    ├───────────────────┤     ├──────────────────┤
    │ id (PK)           │     │ id (PK)          │
    │ user_id (FK)      │  ┌──│ name             │
    │ activity_id (FK)  │──┼──│ destination      │
    │ availability_id   │  │  │ category         │
    │ (FK)              │  │  │ price            │
    │ participants      │  │  │ duration         │
    │ status            │  │  │ availableSlots   │
    └────┬──────────────┘  │  └────────┬─────────┘
         │                 │           │
         │                 │           │ 1:N
         │                 │           │
         │          ┌──────▼────────────▼──┐
         │          │ ActivityAvailability │
         │          ├──────────────────────┤
         │          │ id (PK)              │
         │          │ activity_id (FK)     │
         └──────────│ date                 │
                    │ time                 │
                    │ totalSlots           │
                    │ reservedSlots        │
                    │ (composite key: activity+date+time)
                    └──────────────────────┘
```

### Índices de Optimización

Se agregan índices para mejorar performance en queries frecuentes:

```sql
-- Búsquedas de categoría
CREATE INDEX idx_category ON activities(category);

-- Búsquedas de destino
CREATE INDEX idx_destination ON activities(destination);

-- Búsquedas de precio
CREATE INDEX idx_price ON activities(price);

-- Búsquedas de reservas por usuario
CREATE INDEX idx_reservations_user_status ON reservations(user_id, status);
```

---

## 🔄 FLUJOS PRINCIPALES

### Flujo 1: Registro e Inicio de Sesión

```
┌──────────────────────────────────────┐
│   POST /auth/register                │
│   {email, username, password}        │
└──────────┬───────────────────────────┘
           │
           ▼
    ┌─────────────────────┐
    │ Validar campos      │
    │ - Email no existe   │
    │ - Username no existe│
    └─────────┬───────────┘
              │
              ▼
    ┌─────────────────────┐
    │ HashPassword(BCrypt)│
    └─────────┬───────────┘
              │
              ▼
    ┌─────────────────────┐
    │ Guardar User en BD  │
    └─────────┬───────────┘
              │
              ▼
    ┌──────────────────────────┐
    │ GenerateJWTToken         │
    │ Válido 24 horas          │
    └──────────┬───────────────┘
               │
               ▼
    ┌──────────────────────────┐
    │ Return AuthResponse      │
    │ {token, email, username} │
    └──────────────────────────┘
```

### Flujo 2: Búsqueda de Actividades con Filtros

```
┌────────────────────────────────┐
│ GET /api/v1/activities        │
│ ?category=Adventure            │
│ &destination=Bariloche         │
│ &minPrice=30&maxPrice=100      │
│ &page=0&size=10                │
└────────┬──────────────────────┘
         │
         ▼
    ┌────────────────────────┐
    │ Validar parámetros     │
    │ - page ≥ 0             │
    │ - size entre 1 y 100   │
    └────────┬───────────────┘
             │
             ▼
    ┌────────────────────────────┐
    │ Repository.findByFilters   │
    │ category, destination,     │
    │ minPrice, maxPrice         │
    │ + Pagination (Pageable)    │
    └────────┬───────────────────┘
             │
             ▼
    ┌────────────────────────────┐
    │ Map Activity to DTO        │
    │ ActivityListItemResponse   │
    └────────┬───────────────────┘
             │
             ▼
    ┌────────────────────────────┐
    │ Return Page<Activity>      │
    │ + content                  │
    │ + totalPages               │
    │ + totalElements            │
    │ + pageable metadata        │
    └────────────────────────────┘
```

### Flujo 3: Crear Reserva

```
┌────────────────────────────────┐
│ POST /api/v1/reservations      │
│ Authorization: Bearer TOKEN     │
│ {activityId, date, time, ...}  │
└────────┬──────────────────────┘
         │
         ▼
    ┌──────────────────────────┐
    │ Obtener usuario del JWT  │
    │ SecurityContextHolder    │
    └────────┬─────────────────┘
             │
             ▼
    ┌──────────────────────────┐
    │ Validar actividad existe │
    │ ActivityRepository       │
    └────────┬─────────────────┘
             │
             ▼
    ┌──────────────────────────┐
    │ Validar disponibilidad   │
    │ - Fecha/hora existen     │
    │ - Hay cupos disponibles  │
    └────────┬─────────────────┘
             │
             ▼
    ┌──────────────────────────┐
    │ Validar no hay duplicado │
    │ - No reservó mismo hora  │
    └────────┬─────────────────┘
             │
             ▼
    ┌──────────────────────────┐
    │@Transactional:           │
    │ - Reservar cupos         │
    │ - Crear Reservation      │
    │ - Guardar en BD          │
    └────────┬─────────────────┘
             │
             ▼
    ┌──────────────────────────┐
    │ Return ReservationResponse│
    │ {id, status, ...}        │
    └──────────────────────────┘
```

---

## 🎓 PRINCIPIOS APLICADOS

### SOLID

- **S - Single Responsibility**: Cada clase tiene una responsabilidad
  - `ActivityController`: Mapear HTTP requests
  - `ActivityService`: Lógica de negocio
  - `ActivityRepository`: Queries

- **O - Open/Closed**: Extensible sin modificar código existente
  - Nuevos filtros en ActivityRepository sin cambiar Controller

- **L - Liskov Substitution**: Implementaciones de Repository son intercambiables

- **I - Interface Segregation**: Interfaces específicas
  - `JpaRepository<Activity, Long>` con métodos adonecesarios

- **D - Dependency Inversion**: Inyección de dependencias
  ```java
  public ActivityService(ActivityRepository repo, UserRepository userRepo) {
      this.repo = repo;  // Inyectado, no hardcodeado
  }
  ```

### DRY (Don't Repeat Yourself)
- Métodos reutilizables en services
- Validaciones centralizadas en @Valid
- Enums para status (ReservationStatus)

### KISS (Keep It Simple, Stupid)
- Queries simples y legibles
- Métodos pequeños con responsabilidad única
- Nombres descriptivos

---

## 🚀 OPTIMIZACIONES REALIZADAS

### 1. Paginación
```java
Page<Activity> findAll(Pageable pageable);
```
- Grandes datasets no se cargan completos
- Reduce memoria y ancho de banda

### 2. Query Custom con @Query
```java
@Query("SELECT a FROM Activity a WHERE a.price >= :minPrice AND a.price <= :maxPrice")
Page<Activity> findByPriceRange(...);
```
- Queries optimizadas
- Índices de BD aprovechados

### 3. Transacciones
```java
@Transactional
public ReservationResponse createReservation(...) {
    // Múltiples operaciones atómicas
}
```
- Consistency garantizada
- Rollback automático en errores

### 4. ORM Eager/Lazy Loading
```java
@ManyToOne  // Lazy por defecto
private User user;

@Override
public Reservation getById(Long id) {
    return repo.findById(id).orElseThrow();
    // User se carga cuando se accede
}
```

---

## 📈 ESCALABILIDAD FUTURA

### Mejoras Sugeridas:

1. **Caché**
   ```java
   @Cacheable("activities")
   List<Activity> getAll();
   ```

2. **Búsqueda Full-Text**
   ```sql
   CREATE FULLTEXT INDEX ft_name ON activities(name, description);
   ```

3. **Async Processing**
   ```java
   @Async
   void sendOtpEmail(String email);
   ```

4. **API Versioning**
   ```
   /api/v1/activities
   /api/v2/activities (compatible)
   ```

5. **GraphQL** (alternativa a REST)

6. **Message Queue** (RabbitMQ/Kafka)
   - Procesar reservas asincronicamente

7. **Microservicios**
   - Separar Auth, Activities, Reservations en servicios independientes

---

## ✅ CHECKLIST DE BUENAS PRÁCTICAS

- ✅ DTOs para serialización
- ✅ Validación en DTOs (@Valid)
- ✅ Service layer entre Controller y Repository
- ✅ Transaciones en operaciones críticas
- ✅ Índices en columnas frecuentes
- ✅ Paginaciónen grandes datasets
- ✅ SecurityContext para autenticación
- ✅ Autorización por usuario
- ✅ Hashing de passwords
- ✅ Errores descriptivos
- ✅ Logs adecuados
- ✅ Documentación de API
- ✅ Tests unit/integración

---

## 📚 REFERENCIAS Y DOCUMENTACIÓN

- Spring Boot 3.3.5: https://docs.spring.io/spring-boot/
- Spring Security: https://docs.spring.io/spring-security/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/
- MySQL Best Practices: https://dev.mysql.com/
- JWT.io: https://jwt.io/
