# 📲 ACTUALIZACIÓN BACKEND - IMPLEMENTACIÓN REQUERIDA EN ANDROID STUDIO

**Fecha:** 7 de Abril 2026  
**Estado:** ✅ Backend completado y deployado  
**Repositorio:** https://github.com/WandaSchafer/DA1-back-Grupo5  
**Rama:** `feature/otp-perfil-actividades-v2`  

---

## 🎯 RESUMEN DE CAMBIOS EN BACKEND

El backend ha sido completamente actualizado con:

✅ **Autenticación OTP en MySQL** (no más en memoria)  
✅ **Persistencia de datos de usuario y preferencias**  
✅ **19 actividades cargadas con 118 disponibilidades**  
✅ **Filtros avanzados de actividades**  
✅ **Sistema de recomendaciones personalizado**  
✅ **API de perfil completamente funcional**  
✅ **Logs limpios sin Hibernate queries**  

---

## 🔄 CAMBIOS QUE AFECTAN ANDROID

### 1. OTP AHORA PERSISTE EN BD
**ANTES:** OTP guardado en memoria, perdía validez al reiniciar  
**AHORA:** OTP encriptado en MySQL con validación de 5 minutos  

**Impacto en Android:** 
- El flujo de OTP sigue igual (`POST /auth/otp/request` → `POST /auth/otp/verify`)
- MÁS ESTABLE: El código sigue siendo válido aunque se reinicie la app

### 2. PERFIL DE USUARIO AHORA ACTUALIZABLE
**NUEVO:** Endpoint completo para obtener y actualizar perfil con preferencias

**Endpoints:**
```
GET /api/v1/users/me
Headers: Authorization: Bearer {TOKEN}

PUT /api/v1/users/me
Headers: Authorization: Bearer {TOKEN}
Body: {
  "username": "nuevo_nombre",
  "phone": "+541234567890",
  "profileImageUrl": "https://...",
  "preferences": {
    "preferredCategory": "Adventure",
    "maxPrice": 5000,
    "preferredDestination": "Bariloche",
    "activityDuration": "8-10 horas"
  }
}
```

**Impacto en Android:**
- NECESITA: Pantalla de Perfil editable (ahora funciona!!)
- NECESITA: Mostrar preferencias del usuario
- NECESITA: Permitir editar preferencias

### 3. ACTIVIDADES CON FILTROS AVANZADOS
**AHORA:** 19 actividades con filtros completos funcionales

**Endpoint:**
```
GET /activities?page=0&size=10&category=Adventure&destination=Bariloche&minPrice=0&maxPrice=5000&search=

Parámetros:
- page: página (0-indexed)
- size: cantidad por página
- category: Adventure | Culture | Food | Wellness | Nature
- destination: Bariloche | Buenos Aires | Mendoza | Salta | Puerto Iguazú
- minPrice/maxPrice: rango de precio
- search: búsqueda por nombre
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Trekking Laguna de los Tres Picos",
      "destination": "Bariloche",
      "category": "Adventure",
      "duration": "8-10 horas",
      "price": 4500,
      "availableSlots": 8,
      "imageUrl": "https://images.unsplash.com/..."
    }
  ],
  "totalPages": 2,
  "totalElements": 19,
  "currentPage": 0,
  "pageSize": 10
}
```

**Impacto en Android:**
- NECESITA: Filtros UI (Destino, Categoría, Precio)
- NECESITA: RecyclerView mejorado con CardView
- NECESITA: Bottom Sheet de filtros

### 4. RECOMENDACIONES PERSONALIZADAS
**NUEVO:** Endpoint que cruza preferencias del usuario con actividades

**Endpoint:**
```
GET /activities/recommended
Headers: Authorization: Bearer {TOKEN}
```

**Responde**: Página de actividades filtradas por preferencia del usuario

**Impacto en Android:**
- NUEVO: Sección "Actividades Recomendadas" o "Para Ti"
- Usar preferencias del usuario para mostrar actividades relevantes

---

## 🛠️ CAMBIOS A IMPLEMENTAR EN ANDROID STUDIO

### ✅ PRIORIDAD ALTA (Bloqueantes)

#### 1. **Arreglar Pantalla de Perfil**
- [ ] GET `/api/v1/users/me` → Mostrar datos del usuario
- [ ] Permitir EDITAR: username, phone, foto
- [ ] Mostrar sección "MIS PREFERENCIAS":
  - [ ] Categoría favorita (Spinner)
  - [ ] Presupuesto máximo (RangeSlider)
  - [ ] Destino preferido (Spinner)
  - [ ] Duración de actividades (Spinner)
- [ ] Botón GUARDAR que haga PUT `/api/v1/users/me`
- [ ] Mostrar mensaje de éxito/error

**Código de ejemplo:**
```kotlin
// GET Perfil
val token = sharedPref.getString("jwt_token", "")
apiClient.getProfile("Bearer $token").enqueue(object : Callback<UserProfileResponse> {
    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
        if (response.isSuccessful) {
            val profile = response.body()!!
            usernameEdit.setText(profile.username)
            emailText.text = profile.email
            phoneEdit.setText(profile.phone)
            
            profile.preferences?.let {
                categorySpinner.setSelection(getCategoryIndex(it.preferredCategory))
                budgetSeekbar.progress = (it.maxPrice / 100).toInt()
                destinationSpinner.setSelection(getDestinationIndex(it.preferredDestination))
            }
        }
    }
    
    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
    }
})

// PUT Perfil
val updateRequest = UpdateUserProfileRequest(
    username = usernameEdit.text.toString(),
    phone = phoneEdit.text.toString(),
    preferences = UserPreferenceRequest(
        preferredCategory = categorySpinner.selectedItem.toString(),
        maxPrice = budgetSeekbar.progress * 100,
        preferredDestination = destinationSpinner.selectedItem.toString(),
        activityDuration = durationSpinner.selectedItem.toString()
    )
)

apiClient.updateProfile("Bearer $token", updateRequest).enqueue(object : Callback<UserProfileResponse> {
    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
        if (response.isSuccessful) {
            Toast.makeText(context, "✓ Perfil actualizado", Toast.LENGTH_SHORT).show()
        }
    }
})
```

#### 2. **Implementar Filtros de Actividades**
- [ ] Bottom Sheet con filtros:
  - [ ] Spinner Destino (Bariloche, Buenos Aires, Mendoza, Salta, Puerto Iguazú)
  - [ ] Spinner Categoría (Adventure, Culture, Food, Wellness, Nature)
  - [ ] RangeSlider Precio ($0 - $10,000)
  - [ ] EditText Búsqueda
  - [ ] Botón "APLICAR FILTROS"
- [ ] Cargar actividades con filtros aplicados
- [ ] Mostrar resultados paginados

**Código de ejemplo:**
```kotlin
fun applyFilters(category: String?, destination: String?, minPrice: Int, maxPrice: Int, search: String) {
    val query = mutableListOf<String>()
    
    if (category.isNotEmpty()) query.add("category=$category")
    if (destination.isNotEmpty()) query.add("destination=$destination")
    query.add("minPrice=$minPrice")
    query.add("maxPrice=$maxPrice")
    if (search.isNotEmpty()) query.add("search=$search")
    
    val url = "/activities?${query.joinToString("&")}&page=0&size=10"
    
    apiClient.getActivities(url).enqueue(object : Callback<PagedResponse<ActivityListItemResponse>> {
        override fun onResponse(call: Call<PagedResponse<ActivityListItemResponse>>, response: Response<PagedResponse<ActivityListItemResponse>>) {
            if (response.isSuccessful) {
                val activities = response.body()?.content ?: emptyList()
                updateRecyclerView(activities)
            }
        }
    })
}
```

### 🟡 PRIORIDAD MEDIA (Mejoras UX)

#### 3. **Implementar Modo Oscuro**
- [ ] Agregar toggle Dark Mode en Settings
- [ ] Usar AppCompatDelegate para cambiar tema
- [ ] Persistir preferencia en SharedPreferences
- [ ] Refrescar UI cuando cambias tema

```kotlin
// En MainActivity o Settings
fun toggleDarkMode() {
    val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
    
    AppCompatDelegate.setDefaultNightMode(newMode)
    
    // Guardar preferencia
    SharedPreferences("app_prefs").edit()
        .putBoolean("dark_mode", !isDarkMode)
        .apply()
    
    // Reiniciar activity
    recreate()
}
```

#### 4. **Mejorar CardView de Actividades**
- [ ] Mostrar imagen desde URL (Glide library)
- [ ] Layout mejorado con:
  - [ ] Nombre de actividad (bold, 18sp)
  - [ ] Destino (📍)
  - [ ] Categoría (🎭)
  - [ ] Duración (⏱️)
  - [ ] Precio (destacado, color primario)
  - [ ] Cupos disponibles (👥)
- [ ] Click para ir a detalles
- [ ] Elevation y corner radius mejorado

#### 5. **Nueva Pantalla: Actividades Recomendadas**
- [ ] GET `/activities/recommended` con token
- [ ] Mostrar actividades basadas en preferencias del usuario
- [ ] RecyclerView con mismo layout que actividades
- [ ] Sección en MainActivity o Bottom Navigation

**Código:**
```kotlin
fun loadRecommendedActivities() {
    val token = SharedPreferences("auth").getString("jwt_token", "")
    
    apiClient.getRecommendedActivities("Bearer $token")
        .enqueue(object : Callback<PagedResponse<ActivityListItemResponse>> {
            override fun onResponse(call: Call<PagedResponse<ActivityListItemResponse>>, response: Response<PagedResponse<ActivityListItemResponse>>) {
                if (response.isSuccessful) {
                    val activities = response.body()?.content ?: emptyList()
                    updateRecommendedRecyclerView(activities)
                }
            }
        })
}
```

### 🟢 PRIORIDAD BAJA (Opcionales)

#### 6. **Mostrar Disponibilidades**
- [ ] GET `/activities/{id}` → Mostrar fechas/horarios disponibles
- [ ] Permitir seleccionar fecha para reservar
- [ ] Mostrar cupos disponibles por fecha

#### 7. **Gestión de Reservas**
- [ ] POST `/reservations` con activityId, fecha, participantes
- [ ] GET `/reservations` → Listar mis reservas
- [ ] DELETE `/reservations/{id}` → Cancelar reserva
- [ ] Mostrar estado (CONFIRMADA, CANCELADA, PENDIENTE)

---

## 📊 DATOS DISPONIBLES

**19 Actividades cargadas:**
- 🏔️ 5 Adventure (Bariloche)
- 🏛️ 4 Culture (Buenos Aires)
- 🍷 4 Food (Mendoza)
- 🧘 3 Wellness (Salta)
- 🌿 3 Nature (Puerto Iguazú)

**118 Disponibilidades** (próximas 2 semanas desde hoy)

Todos con imágenes reales de Unsplash

---

## 🧪 TESTING EN ANDROID

### Flujo Completo a Probar:
1. **Login/OTP:**
   - Pedir OTP → Ver código en terminal del backend
   - Ingresar código → Recibir token
   - Guardar token en SharedPreferences

2. **Perfil:**
   - GET `/api/v1/users/me` → Cargar datos
   - Editar username, phone, preferencias
   - PUT → Guardar
   - GET nuevamente → Verificar cambios

3. **Actividades:**
   - GET `/activities` sin filtros → Ver todas
   - Aplicar filtros (category=Adventure, destination=Bariloche)
   - GET con filtros → Verificar que funciona
   - GET `/activities/recommended` → Ver personalizadas

4. **Reservas (Opcional):**
   - Seleccionar actividad y fecha
   - POST `/reservations`
   - GET `/reservations` → Ver mis reservas

---

## 🔗 REFERENCIAS

**Repositorio:** https://github.com/WandaSchafer/DA1-back-Grupo5  
**Rama feature:** `feature/otp-perfil-actividades-v2`  
**Base URL Backend:** `http://localhost:8080` (desarrollo)

**Documentación disponible en repo:**
- `ANDROID-UX-IMPROVEMENTS.md` → Guía completa de implementación
- `API-DOCUMENTATION.md` → Endpoints documentados
- `SETUP-GUIDE.md` → Instalación y configuración

---

## ⚠️ CAMBIOS IMPORTANTES A TENER EN CUENTA

### Cambio en respuesta de Actividades
**ANTES:**
```json
{
  "content": [...]
}
```

**AHORA (pageSerializationMode):**
```json
{
  "content": [...],
  "totalPages": 2,
  "totalElements": 19,
  "currentPage": 0,
  "pageSize": 10
}
```

**Adapta tu Adapter para la nueva estructura:**
```kotlin
data class PagedResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val currentPage: Int,
    val pageSize: Int
)
```

---

## ✅ CHECKLIST IMPLEMENTACIÓN

**Pantalla de Perfil:**
- [ ] GET /api/v1/users/me funciona
- [ ] Mostrar datos en UI
- [ ] Editar campos
- [ ] Mostrar preferencias (4 campos)
- [ ] PUT /api/v1/users/me guarda correctamente
- [ ] Toast de éxito/error

**Filtros de Actividades:**
- [ ] Bottom Sheet con 5 filtros
- [ ] GET /activities con parámetros
- [ ] RecyclerView actualiza correctamente
- [ ] Paginación funciona

**Modo Oscuro:**
- [ ] Toggle en Settings
- [ ] AppCompatDelegate cambia tema
- [ ] Persistencia en SharedPreferences
- [ ] Activity se reinicia con nuevo tema

**Pantalla Recomendada:**
- [ ] GET /activities/recommended con token
- [ ] RecyclerView con actividades personalizadas
- [ ] Mostrada en navegación o tab nuevo

---

## 🚀 DEPLOYMENT

Cuando termines los cambios:
1. Crea un **Pull Request** en el repo de Android
2. Testea completo flujo con backend corriendo
3. Verifica que todas las pantallas funcionen
4. Merge a rama principal

**Backend está 100% listo. Solo espera implementación en Android.**

---

**Última actualización:** 7 de Abril 2026  
**Status:** ✅ Backend Production Ready  
**Android:** ⏳ Esperando implementación
