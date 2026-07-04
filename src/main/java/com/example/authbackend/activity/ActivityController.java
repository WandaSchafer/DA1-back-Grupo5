package com.example.authbackend.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
     */
    @GetMapping
    public Page<Activity> getActivities(
            @Parameter(description = "Número de página (empieza en 0)", required = true) 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Cantidad de elementos por página", required = true) 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Filtrar por categoría (ej: Adventure)") 
            @RequestParam(required = false) String category,
            
            @Parameter(description = "Filtrar por destino (ej: Bariloche)") 
            @RequestParam(required = false) String destination,
            
            @Parameter(description = "Precio mínimo") 
            @RequestParam(required = false) Double minPrice,
            
            @Parameter(description = "Precio máximo") 
            @RequestParam(required = false) Double maxPrice,
            
            @Parameter(description = "Búsqueda por texto en el nombre") 
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));
    }

    /**
     * GET /api/v1/activities/{id}/availability - Disponibilidad de horarios para una actividad
     */
    @Operation(summary = "Obtener horarios disponibles", description = "Devuelve todos los slots libres para una actividad.")
    @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada")
    @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    @GetMapping("/{id}/availability")
    public List<AvailabilitySlotResponse> getAvailability(@PathVariable Long id) {
        return activityAvailabilityService.getAvailability(id);
    }
}