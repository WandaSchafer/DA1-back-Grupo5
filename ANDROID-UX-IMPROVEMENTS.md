# 🎨 MEJORAS UX PARA ANDROID STUDIO

## Backend está listo ✓
- OTP limpio (sin logs de Hibernate)
- Perfil completamente funcional con logs detallados
- 19 actividades con 118 disponibilidades cargadas

---

## 1️⃣ IMPLEMENTAR MODO OSCURO

### En `styles.xml` (valores/styles.xml)
```xml
<resources>
    <!-- Tema Claro -->
    <style name="Theme.AppLight" parent="Theme.MaterialComponents.Light">
        <item name="colorPrimary">@color/primary_light</item>
        <item name="colorSecondary">@color/secondary_light</item>
        <item name="colorBackground">@color/bg_light</item>
        <item name="android:textColor">@color/text_dark</item>
    </style>

    <!-- Tema Oscuro -->
    <style name="Theme.AppDark" parent="Theme.MaterialComponents">
        <item name="colorPrimary">@color/primary_dark</item>
        <item name="colorSecondary">@color/secondary_dark</item>
        <item name="colorBackground">@color/bg_dark</item>
        <item name="android:textColor">@color/text_light</item>
    </style>
</resources>
```

### En `colors.xml` (valores/colors.xml)
```xml
<resources>
    <!-- Colores Tema Claro -->
    <color name="primary_light">#1976D2</color>
    <color name="secondary_light">#FF6D00</color>
    <color name="bg_light">#FFFFFF</color>
    <color name="text_dark">#212121</color>
    <color name="card_light">#F5F5F5</color>

    <!-- Colores Tema Oscuro -->
    <color name="primary_dark">#90CAF9</color>
    <color name="secondary_dark">#FFB74D</color>
    <color name="bg_dark">#121212</color>
    <color name="text_light">#E0E0E0</color>
    <color name="card_dark">#1E1E1E</color>
</resources>
```

### En `MainActivity.kt` (o tu Activity principal)
```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Detectar preferencia de tema del usuario
        val isDarkMode = SharedPreferences("app_prefs")
            .getBoolean("dark_mode", false)
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    
    // Método para cambiar tema desde Configuraciones
    fun toggleDarkMode() {
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        // Guardar preferencia
        SharedPreferences("app_prefs").edit().putBoolean("dark_mode", !isDarkMode).apply()
        
        // Reiniciar activity para aplicar tema
        recreate()
    }
}
```

---

## 2️⃣ ARREGLAR PANTALLA DE PERFIL

### El Backend responde correctamente a:
```kotlin
// GET perfil
val client = RetrofitClient.getClient()
val call = client.getProfile("Bearer $token")

call.enqueue(object : Callback<UserProfileResponse> {
    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
        if (response.isSuccessful) {
            val profile = response.body()
            // Mostrar datos
            username.setText(profile?.username)
            email.text = profile?.email
            phone.setText(profile?.phone)
            
            // Mostrar preferencias
            profile?.preferences?.let {
                categorySpinner.setSelection(getCategoryIndex(it.preferredCategory))
                budgetSeekbar.progress = (it.maxPrice / 100).toInt()
                destinationSpinner.setSelection(getDestinationIndex(it.preferredDestination))
                durationSpinner.setSelection(getDurationIndex(it.activityDuration))
            }
        }
    }
    
    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
        Log.e("PROFILE", "Error: ${t.message}")
    }
})
```

### Actualizar perfil correctamente:
```kotlin
// PUT perfil con preferencias
val updateRequest = UpdateUserProfileRequest(
    username = "new_name",
    phone = "+541234567890",
    profileImageUrl = "https://...",
    preferences = UserPreferenceRequest(
        preferredCategory = "Adventure",
        maxPrice = 5000,
        preferredDestination = "Bariloche",
        activityDuration = "8-10 horas"
    )
)

val call = client.updateProfile("Bearer $token", updateRequest)

call.enqueue(object : Callback<UserProfileResponse> {
    override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
        if (response.isSuccessful) {
            Toast.makeText(context, "✓ Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            // Volver a cargar perfil o cerrar modal
        } else {
            Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
        }
    }
})
```

---

## 3️⃣ MEJORAR LAYOUT DE ACTIVIDADES

### RecyclerView para Actividades
```kotlin
data class ActivityListItemResponse(
    val id: Long,
    val name: String,
    val destination: String,
    val category: String,
    val duration: String,
    val price: Int,
    val availableSlots: Int,
    val imageUrl: String
)

// Activity Adapter con mejor visual
class ActivityAdapter(
    private val activities: List<ActivityListItemResponse>,
    private val onItemClick: (ActivityListItemResponse) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        
        holder.itemView.apply {
            findViewById<ImageView>(R.id.activityImage).apply {
                Glide.with(context)
                    .load(activity.imageUrl)
                    .centerCrop()
                    .into(this)
            }
            
            findViewById<TextView>(R.id.activityName).text = activity.name
            findViewById<TextView>(R.id.destination).text = "📍 ${activity.destination}"
            findViewById<TextView>(R.id.category).text = "🎭 ${activity.category}"
            findViewById<TextView>(R.id.duration).text = "⏱️ ${activity.duration}"
            findViewById<TextView>(R.id.price).text = "$${activity.price.toString()}"
            findViewById<TextView>(R.id.slots).text = "👥 ${activity.availableSlots} cupos"
            
            setOnClickListener { onItemClick(activity) }
        }
    }

    override fun getItemCount() = activities.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
```

### Layout de Card (item_activity_card.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardElevation="4dp"
    app:cardCornerRadius="12dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- IMAGEN -->
        <ImageView
            android:id="@+id/activityImage"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:scaleType="centerCrop"
            android:src="@drawable/placeholder" />

        <!-- INFO -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <TextView
                android:id="@+id/activityName"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="18sp"
                android:textStyle="bold"
                android:maxLines="2"
                android:ellipsize="end" />

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:layout_marginTop="8dp"
                android:gravity="center_vertical">

                <TextView
                    android:id="@+id/destination"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:textSize="12sp" />

                <TextView
                    android:id="@+id/category"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="12sp"
                    android:layout_marginStart="8dp" />
            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:layout_marginTop="8dp"
                android:gravity="space_between">

                <LinearLayout
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:orientation="vertical">
                    
                    <TextView
                        android:id="@+id/duration"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textSize="11sp" />

                    <TextView
                        android:id="@+id/slots"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textSize="11sp"
                        android:layout_marginTop="4dp" />
                </LinearLayout>

                <LinearLayout
                    android:layout_width="wrap_content"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:gravity="bottom">

                    <TextView
                        android:id="@+id/price"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:textSize="16sp"
                        android:textStyle="bold"
                        android:textColor="@color/primary_light" />
                </LinearLayout>
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
</com.google.android.material.card.MaterialCardView>
```

---

## 4️⃣ PANTALLA DE ACTIVIDADES DESTACADAS

### Fragment para Actividades Recomendadas
```kotlin
class RecommendedActivitiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private val apiClient = RetrofitClient.getClient()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommended, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        loadRecommendedActivities()
    }

    private fun loadRecommendedActivities() {
        val token = SharedPreferences("auth").getString("jwt_token", "")
        
        apiClient.getRecommendedActivities("Bearer $token")
            .enqueue(object : Callback<PagedResponse<ActivityListItemResponse>> {
                override fun onResponse(
                    call: Call<PagedResponse<ActivityListItemResponse>>,
                    response: Response<PagedResponse<ActivityListItemResponse>>
                ) {
                    if (response.isSuccessful) {
                        val activities = response.body()?.content ?: emptyList()
                        adapter = ActivityAdapter(activities) { activity ->
                            navigateToDetail(activity.id)
                        }
                        recyclerView.adapter = adapter
                    }
                }

                override fun onFailure(
                    call: Call<PagedResponse<ActivityListItemResponse>>,
                    t: Throwable
                ) {
                    Log.e("RECOMMENDED", "Error: ${t.message}")
                    Toast.makeText(context, "Error cargando actividades", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToDetail(activityId: Long) {
        // Navegar a detalles
    }
}
```

---

## 5️⃣ PANTALLA DE FILTROS

### Filtros Avanzados
```kotlin
class ActivityFiltersBottomSheet : BottomSheetDialogFragment() {

    private lateinit var categorySpinner: Spinner
    private lateinit var destinationSpinner: Spinner
    private lateinit var priceSeekBar: RangeSlider
    private lateinit var searchEdit: EditText
    private var onFilterApply: ((filters: FilterParams) -> Unit)? = null

    data class FilterParams(
        val category: String? = null,
        val destination: String? = null,
        val minPrice: Int = 0,
        val maxPrice: Int = 10000,
        val search: String = ""
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Spinners con opciones
        val categories = listOf("", "Adventure", "Culture", "Food", "Wellness", "Nature")
        val destinations = listOf("", "Bariloche", "Buenos Aires", "Mendoza", "Salta", "Puerto Iguazú")
        
        categorySpinner = view.findViewById(R.id.categorySpinner)
        destinationSpinner = view.findViewById(R.id.destinationSpinner)
        priceSeekBar = view.findViewById(R.id.priceRange)
        searchEdit = view.findViewById(R.id.searchEdit)
        
        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        destinationSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, destinations)
        
        // RangeSlider para precio
        priceSeekBar.setValues(0f, 10000f)
        priceSeekBar.setLabelFormatter { value: Float -> "$${value.toInt()}" }
        
        // Botón aplicar filtros
        view.findViewById<Button>(R.id.applyButton).setOnClickListener {
            val filters = FilterParams(
                category = categorySpinner.selectedItem.toString().takeIf { it.isNotEmpty() },
                destination = destinationSpinner.selectedItem.toString().takeIf { it.isNotEmpty() },
                minPrice = priceSeekBar.values[0].toInt(),
                maxPrice = priceSeekBar.values[1].toInt(),
                search = searchEdit.text.toString()
            )
            onFilterApply?.invoke(filters)
            dismiss()
        }
    }

    fun setOnFilterApply(callback: (FilterParams) -> Unit) {
        onFilterApply = callback
    }
}
```

---

## ✅ CHECKLIST ANDROID

- [ ] Modo oscuro implementado (AppCompatDelegate)
- [ ] RecyclerView de actividades con CardView mejorado
- [ ] Pantalla de Perfil con actualización funcional
- [ ] Pantalla de Recomendaciones (GET /activities/recommended)
- [ ] Bottom Sheet de Filtros con spinners
- [ ] Botón toggle Dark Mode en Settings
- [ ] Glide para cargar imágenes desde URLs
- [ ] Mejor UX con Material Design 3
- [ ] SharedPreferences para guardar preferencias
- [ ] Validación de datos antes de enviar al backend

---

## 📞 BACKEND STATUS

```
✓ OTP con logs limpios (sin Hibernate)
✓ Perfil GET/PUT funcional
✓ 19 actividades cargadas
✓ Filtros avanzados disponibles
✓ Recomendaciones personalizadas ready
✓ Dark mode ready en backend (respeta tema)
```

**Próximo paso:** Implementa esta configuración en Android Studio y testea con el backend corriendo.
