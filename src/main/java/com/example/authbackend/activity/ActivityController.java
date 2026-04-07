package com.example.authbackend.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@CrossOrigin
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final ActivityAvailabilityService activityAvailabilityService;
    private final ActivityService activityService;

    public ActivityController(ActivityRepository activityRepository,
                              ActivityAvailabilityService activityAvailabilityService,
                              ActivityService activityService) {
        this.activityRepository = activityRepository;
        this.activityAvailabilityService = activityAvailabilityService;
        this.activityService = activityService;
    }

    /**
     * GET /api/v1/activities - Lista paginada de actividades con filtros opcionales
     * Parámetros:
     * - page: número de página (default: 0)
     * - size: cantidad de elementos por página (default: 10)
     * - category: filtrar por categoría
     * - destination: filtrar por destino
     * - minPrice: precio mínimo
     * - maxPrice: precio máximo
     * - search: búsqueda por nombre
     */
    @GetMapping
    public Page<Activity> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String search
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        // Si hay búsqueda por nombre, usarla
        if (search != null && !search.isBlank()) {
            return activityRepository.findByNameIgnoreCaseContaining(search, pageRequest);
        }

        // Si hay filtros, usarlos
        if ((category != null && !category.isBlank()) ||
                (destination != null && !destination.isBlank()) ||
                minPrice != null || maxPrice != null) {
            return activityRepository.findByFilters(category, destination, minPrice, maxPrice, pageRequest);
        }

        // Si no hay filtros, retornar todas
        return activityRepository.findAll(pageRequest);
    }

    /**
     * GET /api/v1/activities/recommended - Actividades recomendadas basadas en preferencias del usuario autenticado
     */
    @GetMapping("/recommended")
    public List<ActivityListItemResponse> getRecommendedActivities() {
        return activityService.getRecommendedActivities();
    }

    /**
     * GET /api/v1/activities/{id} - Detalles de una actividad específica
     */
    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }

    /**
     * GET /api/v1/activities/{id}/availability - Disponibilidad de horarios para una actividad
     */
    @GetMapping("/{id}/availability")
    public List<AvailabilitySlotResponse> getAvailability(@PathVariable Long id) {
        return activityAvailabilityService.getAvailability(id);
    }
}