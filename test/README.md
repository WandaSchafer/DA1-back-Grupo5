# XploreNow — Archivos .http para testear el backend

## Requisitos
- **VS Code** con la extensión **REST Client** (humao.rest-client)

## Cómo ejecutar
1. Levantá el backend en `localhost:8080`.
2. Abrí cualquier `.http` y hacé clic en "Send Request" arriba de cada bloque (separado por `###`).
3. Ejecutá los requests **de arriba hacia abajo dentro del mismo archivo**: la mayoría hace login primero y usa `@token` capturado automáticamente en las siguientes llamadas.

## Único paso manual: OTP
En `01-auth.http`, el endpoint `/auth/otp/verify` necesita el código real de 6 dígitos que tu backend genera (normalmente lo vas a ver en la consola/log del server, o en la tabla de OTPs si lo persistís en la DB). Reemplazá `"123456"` por ese valor antes de correr ese request.

## Estructura (mapeada a los puntos del enunciado del TP)

| Archivo | Punto del TP | Endpoints |
|---|---|---|
| `01-auth.http` | 1. Autenticación y Registro | register, otp/request, otp/verify, otp/resend, login |
| `02-profile.http` | 2. Perfil del Viajero | GET/PUT `/api/v1/users/me` |
| `03-activities.http` | 3. Catálogo de Actividades | list (paginado+filtros), detalle, availability, recommended |
| `04-reservations.http` | 4. Reservas + 11. Voucher/QR | create, get, cancel, reschedule, check-in, me, updates |
| `05-history.http` | 5. Historial de Actividades | GET `/api/v1/history` (filtros por fecha y destino) |
| `06-ratings.http` | 6. Calificación de Actividades y Guías | create, list por actividad, stats, my-ratings, delete |
| `07-news.http` | 9. Noticias, Ofertas y Destinos | GET `/api/v1/news` |

**Total: 24 endpoints cubiertos**, cada uno con al menos un caso feliz y, donde tiene sentido, casos de error (401 sin token, 404 not found, 400 por
validación de campos según los `@pattern`/`min`/`max` del OpenAPI).

## Cosas para chequear en tu backend mientras testeás
- **Autorización real vs solo autenticación**: en `06-ratings.http` (5.c) dejé   un caso para verificar que un usuario no pueda borrar la calificación de otro   (no lo pude armar 100% porque no tengo un segundo usuario/id real, ajustalo con datos tuyos).
- **`checkUpdates`** (`/api/v1/reservations/updates`) es clave para el punto 8 (modo offline → sincronización). Fijate qué trae exactamente el body de
  respuesta (el swagger lo tipa como `object` genérico) para saber cómo consumirlo desde la app.
- **Endpoints "públicos" vs con token**: dejé variantes sin `Authorization` en `activities/recommended` y `news` para confirmar cuáles requieren sesión
  y cuáles no (el enunciado sugiere que catálogo y noticias podrían ser públicos, pero "recommended" depende de las preferencias del perfil, así que probablemente sí necesite token).
- Los `id` de actividad/reserva se encadenan automáticamente desde las respuestas anteriores (`@activityId`, `@reservationId`), no hace falta que los pongas a mano salvo que quieras apuntar a algo específico.