# 🎯 Backend de Actividades y Reservas - Spring Boot

> Aplicación Backend para gestionar actividades turísticas, preferencias de usuario y reservas. Completamente integrada con la app Android del equipo.

## ✨ Características Principales

- **🔐 Autenticación Segura**: JWT + OTP en BD (migrado de memoria)
- **🎯 Recomendaciones Inteligentes**: Basadas en preferencias del usuario
- **🏃 Búsqueda Avanzada**: Filtros por categoría, destino, precio
- **📅 Gestión de Reservas**: Create, Read, Cancel con validaciones completas
- **👤 Perfil de Usuario**: Preferencias estructuradas y datos personales
- **📊 Paginación**: Para datasets grandes
- **🔒 Seguridad**: Authorization por usuario, no accede recursos ajenos

## 🚀 Quick Start

### 1. Clonar y Configurar
```bash
git clone https://github.com/WandaSchafer/DA1-back-Grupo5.git
cd DA1-back-Grupo5

# Editar application.properties con tu BD
nano src/main/resources/application.properties
```

### 2. Crear BD y Ejecutar Script
```bash
mysql -u root -p
CREATE DATABASE DAI;
USE DAI;
source database-schema.sql;
```

### 3. Compilar y Ejecutar
```bash
mvn clean install
mvn spring-boot:run

# O directamente
java -jar target/auth-backend-0.0.1-SNAPSHOT.jar
```

### 4. Verificar que Funciona
```bash
curl http://localhost:8080/api/v1/activities
```

## 📚 Documentación

### Para Comenzar Rápido 👈
1. **[RESUMEN-IMPLEMENTACION.md](RESUMEN-IMPLEMENTACION.md)** ← LEE ESTO PRIMERO
   - Qué se implementó
   - Cambios principales
   - Métricas

### Para Configurar y Ejecutar
2. **[SETUP-GUIDE.md](SETUP-GUIDE.md)**
   - Pasos detallados de instalación
   - Solución de problemas
   - Comandos Maven

### Para Usar los Endpoints
3. **[API-DOCUMENTATION.md](API-DOCUMENTATION.md)**
   - Todos los endpoints documentados
   - Ejemplos de requests/responses
   - Parámetros explicados

### Para Testing
4. **[TESTING-GUIDE.md](TESTING-GUIDE.md)**
   - Casos de prueba completos
   - Flujo de testing recomendado
   - Validaciones

### Para Arquitectura y Patrones
5. **[ARCHITECTURE.md](ARCHITECTURE.md)**
   - Diagrama del sistema
   - Patrones de diseño usados
   - Modelo de datos ER
   - Decisiones arquitectónicas

### SQL de Base de Datos
6. **[database-schema.sql](database-schema.sql)**
   - Script completo para crear tablas
   - Índices optimizados
   - Datos de prueba

---

## 🎓 Tecnologías

| Tech | Version | Propósito |
|------|---------|-----------|
| Java | 17 | Lenguaje |
| Spring Boot | 3.3.5 | Framework |
| MySQL | 8.0+ | Base de datos |
| Maven | 3.8+ | Build |
| JWT | 0.12.6 | Autenticación |
| Hibernate | 6.4 | ORM |
| Spring Security | 6.2 | Seguridad |

---

## 📦 Lo Nuevo en Esta Versión ✅

### Nuevas Tablas (BD)
- `user_preferences` - Preferencias estructuradas
- `otp_entries` - OTP persistido en BD

### Nuevas Entidades (Java)
- `UserPreference.java`
- `OtpEntry.java` (ahora @Entity JPA)

### Nuevos Repositories
- `UserPreferenceRepository.java`
- `OtpEntryRepository.java`

### Nuevos DTOs
- `UserPreferenceResponse.java`
- `UserPreferenceRequest.java`

### Endpoints Mejorados
- `GET /api/v1/activities` - Con filtros avanzados
- `GET /api/v1/activities/recommended` - Usa usuario autenticado
- `PUT /api/v1/users/me` - Maneja preferencias
- `POST /api/v1/reservations` - Mayor validación
- `PATCH /api/v1/reservations/{id}/cancel` - Con autorización

---

## 🔌 Endpoints Principales

```
AUTENTICACIÓN
  POST   /auth/register                 - Registrarse
  POST   /auth/login                    - Iniciar sesión
  POST   /auth/otp/request              - Solicitar OTP
  POST   /auth/otp/verify               - Verificar OTP

ACTIVIDADES
  GET    /api/v1/activities             - Listar (con filtros)
  GET    /api/v1/activities/recommended - Recomendadas (auth)
  GET    /api/v1/activities/{id}        - Detalles
  GET    /api/v1/activities/{id}/availability - Horarios

PERFIL
  GET    /api/v1/users/me               - Mi perfil (auth)
  PUT    /api/v1/users/me               - Actualizar perfil (auth)

RESERVAS
  POST   /api/v1/reservations           - Crear reserva (auth)
  GET    /api/v1/reservations/me        - Mis reservas (auth)
  PATCH  /api/v1/reservations/{id}/cancel - Cancelar (auth)
```

---

## 🧪 Testing

### Flujo Rápido de Prueba
```bash
# 1. Registrar usuario
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","username":"testuser","password":"Pass123!"}'

# 2. Listar actividades (sin auth)
curl http://localhost:8080/api/v1/activities

# 3. Filtrar actividades
curl 'http://localhost:8080/api/v1/activities?category=Adventure&minPrice=30&maxPrice=100'
```

---

## 🏗️ Estructura del Proyecto

```
src/main/java/com/example/authbackend/
├── activity/       # Controladores/Servicios de Actividades
├── user/           # Usuarios y Perfil
├── auth/           # Autenticación
├── otp/            # OTP (ahora en BD)
├── reservation/    # Reservas
├── security/       # Seguridad
└── exception/      # Manejo de errores
```

---

## 📌 Links Útiles

- **Repo Backend**: https://github.com/WandaSchafer/DA1-back-Grupo5
- **Repo Android**: https://github.com/SanTegli/DA1-android-Grupo5
- **Spring Boot Docs**: https://docs.spring.io/spring-boot/
- **JWT**: https://jwt.io/

---

## 🎉 ¡Listo para Empezar!

1. Lee **[RESUMEN-IMPLEMENTACION.md](RESUMEN-IMPLEMENTACION.md)**
2. Sigue pasos en **[SETUP-GUIDE.md](SETUP-GUIDE.md)**
3. Prueba endpoints en **[API-DOCUMENTATION.md](API-DOCUMENTATION.md)**

**¡Buena suerte! 🚀**
