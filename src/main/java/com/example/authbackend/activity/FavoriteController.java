package com.example.authbackend.activity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/favorites")
@CrossOrigin
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    /**
     * POST /api/v1/favorites/{activityId} - Marcar una actividad como favorita
     * Requiere: Authorization Bearer token
     */
    @PostMapping("/{activityId}")
    public ResponseEntity<FavoriteResponse> addFavorite(@PathVariable Long activityId) {
        FavoriteResponse response = favoriteService.addFavorite(activityId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * DELETE /api/v1/favorites/{activityId} - Remover una actividad de favoritos
     * Requiere: Authorization Bearer token
     */
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Map<String, String>> removeFavorite(@PathVariable Long activityId) {
        favoriteService.removeFavorite(activityId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Actividad removida de favoritos");
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/favorites - Obtener todos los favoritos del usuario
     * Requiere: Authorization Bearer token
     */
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getUserFavorites() {
        List<FavoriteResponse> favorites = favoriteService.getUserFavorites();
        return ResponseEntity.ok(favorites);
    }

    /**
     * GET /api/v1/favorites/changes/price - Obtener favoritos con cambios de precio
     * Requiere: Authorization Bearer token
     */
    @GetMapping("/changes/price")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesWithPriceChanges() {
        List<FavoriteResponse> favorites = favoriteService.getFavoritesWithPriceChanges();
        return ResponseEntity.ok(favorites);
    }

    /**
     * GET /api/v1/favorites/changes/availability - Obtener favoritos con cambios de disponibilidad
     * Requiere: Authorization Bearer token
     */
    @GetMapping("/changes/availability")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesWithAvailabilityChanges() {
        List<FavoriteResponse> favorites = favoriteService.getFavoritesWithAvailabilityChanges();
        return ResponseEntity.ok(favorites);
    }

    /**
     * GET /api/v1/favorites/changes/any - Obtener favoritos con cualquier cambio
     * Requiere: Authorization Bearer token
     */
    @GetMapping("/changes/any")
    public ResponseEntity<List<FavoriteResponse>> getFavoritesWithChanges() {
        List<FavoriteResponse> favorites = favoriteService.getFavoritesWithChanges();
        return ResponseEntity.ok(favorites);
    }

    /**
     * GET /api/v1/favorites/{activityId}/check - Verificar si una actividad está marcada como favorita
     * Requiere: Authorization Bearer token
     */
    @GetMapping("/{activityId}/check")
    public ResponseEntity<Map<String, Object>> checkIsFavorite(@PathVariable Long activityId) {
        boolean isFavorite = favoriteService.isFavorite(activityId);
        Map<String, Object> response = new HashMap<>();
        response.put("activityId", activityId);
        response.put("isFavorite", isFavorite);
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/favorites/{favoriteId}/clear-price-change - Limpiar indicador de cambio de precio
     * Requiere: Authorization Bearer token
     */
    @PatchMapping("/{favoriteId}/clear-price-change")
    public ResponseEntity<Map<String, String>> clearPriceChangeIndicator(@PathVariable Long favoriteId) {
        favoriteService.clearPriceChangeIndicator(favoriteId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Indicador de cambio de precio limpiado");
        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/v1/favorites/{favoriteId}/clear-availability-change - Limpiar indicador de cambio de disponibilidad
     * Requiere: Authorization Bearer token
     */
    @PatchMapping("/{favoriteId}/clear-availability-change")
    public ResponseEntity<Map<String, String>> clearAvailabilityChangeIndicator(@PathVariable Long favoriteId) {
        favoriteService.clearAvailabilityChangeIndicator(favoriteId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Indicador de cambio de disponibilidad limpiado");
        return ResponseEntity.ok(response);
    }
}
