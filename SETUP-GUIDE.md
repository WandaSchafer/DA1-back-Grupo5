# 🚀 GUÍA DE CONFIGURACIÓN Y EJECUCIÓN

## Prerequisitos

- **Java 17+** (compilado con Java 17)
- **Maven 3.8+** (o mvnw wrapper)
- **MySQL 8.0+**
- **Postman o Insomnia** (para probar endpoints)

---

## 1️⃣ CONFIGURACIÓN DE BASE DE DATOS

### Paso 1: Crear base de datos
```sql
CREATE DATABASE DAI;
USE DAI;
```

### Paso 2: Ejecutar script SQL
```bash
mysql -u root -p DAI < database-schema.sql
```

**O en MySQL Workbench:**
1. Abrir `database-schema.sql`
2. Ejecutar el script completo
3. Verificar que se crearon las tablas:
   ```sql
   SHOW TABLES;
   ```

---

## 2️⃣ CONFIGURACIÓN DE PROPIEDADES

### Editar `application.properties`

Ubicación: `src/main/resources/application.properties`

```properties
# Configuración MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/DAI?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_password_mysql

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
app.jwt.secret=TuClaveSecretaMuyLargaAquiQueDebeTener64Caracteres
app.jwt.expiration-ms=86400000  # 24 horas

# OTP (ahora en BD)
app.otp.expiration-ms=300000    # 5 minutos
```

---

## 3️⃣ COMPILACIÓN DEL PROYECTO

### Opción A: Con Maven instalado globalmente
```bash
cd "c:\Users\User\Documents\UADE\Desarollo Aplicaciones I\BACK\DA1-back-Grupo5"
mvn clean install
```

### Opción B: Con Maven Wrapper (si existe)
```bash
cd "c:\Users\User\Documents\UADE\Desarollo Aplicaciones I\BACK\DA1-back-Grupo5"
./mvnw clean install  # Linux/Mac
mvnw.cmd clean install  # Windows
```

### Opción C: Solo compilar (sin ejecutar tests)
```bash
mvn clean compile -DskipTests
```

**Output esperado:**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  XX.XXs
[INFO] Finished at: 2025-04-07T...
```

---

## 4️⃣ EJECUCIÓN DEL PROYECTO

### Opción A: Desde IDE (VS Code/IntelliJ)
1. Abrir el proyecto en VS Code
2. Instalar extension: "Extension Pack for Java"
3. Buscar la clase `AuthBackendApplication.java`
4. Hacer clic derecho → "Run" o usar F5

### Opción B: Desde terminal
```bash
mvn spring-boot:run
```

### Opción C: Ejecutar JAR
```bash
java -jar target/auth-backend-0.0.1-SNAPSHOT.jar
```

**Output esperado:**
```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.5)

2025-04-07 10:30:00.000  INFO 12345 --- [main] c.e.a.AuthBackendApplication            : Starting AuthBackendApplication...
...
2025-04-07 10:30:05.000  INFO 12345 --- [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080
```

### Servidor ejecutándose en:
```
http://localhost:8080
```

---

## 5️⃣ VERIFICACIÓN DE FUNCIONAMIENTO

### ✅ Test 1: Health Check
```bash
curl http://localhost:8080/auth/register -X OPTIONS
# Debe retornar 200 OK (CORS preflight)
```

### ✅ Test 2: Registrar usuario
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "username": "testuser",
    "password": "Password123!"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "test@example.com",
  "username": "testuser"
}
```

### ✅ Test 3: Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123!"
  }'
```

### ✅ Test 4: Listar actividades
```bash
curl "http://localhost:8080/api/v1/activities?page=0&size=10"
```

### ✅ Test 5: Con filtros
```bash
curl "http://localhost:8080/api/v1/activities?category=Adventure&minPrice=40&maxPrice=100"
```

### ✅ Test 6: Perfil (con autenticación)
```bash
curl -H "Authorization: Bearer TU_JWT_TOKEN" \
  http://localhost:8080/api/v1/users/me
```

---

## 🔧 SOLUCIÓN DE PROBLEMAS

### Error: "Cannot find module 'mysql'"
**Problema**: Falta el driver MySQL
**Solución**:
```bash
mvn dependency:resolve
```

### Error: "Port 8080 already in use"
**Problema**: Otro proceso usa el puerto 8080
**Solución** 1: Cambiar puerto
```properties
server.port=8081
```

**Solución** 2: Matar proceso
```bash
# Linux/Mac
lsof -i :8080 | grep LISTEN | awk '{print $2}' | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Error: "Access denied for user 'root'@'localhost'"
**Problema**: Contraseña MySQL incorrecta
**Solución**: Actualizar en `application.properties`

### Error: "Table 'DAI.users' doesn't exist"
**Problema**: No se ejecutó el script SQL
**Solución**: Ejecutar `database-schema.sql` manualmente

### Error: "JWT token is invalid or expired"
**Problema**: Token vencido (>24 horas) o secret incorrecto
**Solución**: 
1. Generar nuevo token
2. Verificar `app.jwt.secret` en properties

---

## 📊 MONITOREO EN DESARROLLO

### Habilitar logs detallados
En `application.properties`:
```properties
logging.level.root=INFO
logging.level.com.example.authbackend=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Ver logs de SQL
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## 🧪 TESTING CON POSTMAN

### Importar colección
1. Crear nueva carpeta en Postman: "DA1 Backend"
2. Crear requests según API-DOCUMENTATION.md

**Ejemplo de flow:**
1. `POST /auth/register` → Obtener token
2. `GET /api/v1/activities` → Listar actividades
3. `GET /api/v1/activities/1/availability` → Ver disponibilidad
4. `POST /api/v1/reservations` → Crear reserva (con token)
5. `GET /api/v1/reservations/me` → Ver mis reservas (con token)

---

## 📦 ESTRUCTURA DE DIRECTORIOS

```
src/main/java/com/example/authbackend/
├── activity/                    # Controladores/Servicios de Actividades
│   ├── ActivityController.java
│   ├── ActivityService.java
│   ├── ActivityRepository.java
│   ├── Activity.java           # Entidad JPA
│   └── ActivityListItemResponse.java
├── user/                        # Usuarios y Perfil
│   ├── UserController.java
│   ├── UserService.java
│   ├── User.java              # Entidad JPA
│   ├── UserPreference.java    # Entidad (NUEVA)
│   ├── UserPreferenceRepository.java # (NUEVA)
│   └── dto/
│       ├── UserProfileResponse.java
│       ├── UpdateUserProfileRequest.java
│       ├── UserPreferenceResponse.java
│       └── UserPreferenceRequest.java
├── auth/                        # Autenticación
│   ├── AuthController.java
│   ├── AuthService.java
│   └── dto/
├── otp/                         # OTP (ahora en BD)
│   ├── OtpService.java
│   ├── OtpStore.java           # Actualizado
│   ├── OtpEntry.java           # Ahora es @Entity
│   └── OtpEntryRepository.java  # (NUEVO)
├── reservation/                 # Reservas
│   ├── ReservationController.java
│   ├── ReservationService.java
│   └── Reservation.java
├── security/                    # Configuración Spring Security
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
└── exception/                   # Manejo de excepciones
    └── GlobalExceptionHandler.java
```

---

## 🎯 PRÓXIMAS MEJORAS SUGERIDAS

1. **Paginación mejorada** en recomendaciones
2. **Cache** para actividades frecuentes
3. **Notificaciones** cuando hay nuevas actividades
4. **Rating/Reviews** de actividades y usuarios
5. **Payment integration** (Stripe/Mercado Pago)
6. **Docker** para containerización
7. **Tests** unitarios y de integración

---

## ✉️ CONTACTO

- **Backend**: https://github.com/WandaSchafer/DA1-back-Grupo5
- **Frontend Android**: https://github.com/SanTegli/DA1-android-Grupo5
