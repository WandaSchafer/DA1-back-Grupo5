package com.example.authbackend.rating;

import com.example.authbackend.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ratings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RatingController {

    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;

    /**
     * Obtiene el usuario autenticado desde SecurityContext
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof com.example.authbackend.security.user.CustomUserDetails customUserDetails) {
            return customUserDetails.getUser(); // Asumiendo que CustomUserDetails tiene getUser()
        }
        throw new RuntimeException("Usuario no autenticado");
    }

    /**
     * Crea una nueva calificación para una actividad completada.
     * POST /api/v1/ratings/activity/{activityId}
     */
    @PostMapping("/activity/{activityId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createRating(
            @PathVariable Long activityId,
            @Valid @RequestBody CreateRatingRequest request) {

        try {
            logger.info("POST /api/v1/ratings/activity/{} - Creating rating", activityId);

            User currentUser = getAuthenticatedUser();
            RatingResponse response = ratingService.createRating(activityId, currentUser, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating rating for activity {}: {}", activityId, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Obtiene todas las calificaciones de una actividad.
     * GET /api/v1/ratings/activity/{activityId}
     */
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<?> getRatingsByActivity(@PathVariable Long activityId) {
        try {
            logger.info("GET /api/v1/ratings/activity/{} - Fetching ratings", activityId);
            
            List<RatingResponse> ratings = ratingService.getRatingsByActivity(activityId);
            return ResponseEntity.ok(ratings);
            
        } catch (Exception e) {
            logger.error("Error fetching ratings for activity {}: {}", activityId, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Obtiene estadísticas de calificación para una actividad.
     * GET /api/v1/ratings/activity/{activityId}/stats
     */
    @GetMapping("/activity/{activityId}/stats")
    public ResponseEntity<?> getRatingStats(@PathVariable Long activityId) {
        try {
            logger.info("GET /api/v1/ratings/activity/{}/stats - Fetching stats", activityId);
            
            RatingStatsResponse stats = ratingService.getRatingStats(activityId);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error fetching stats for activity {}: {}", activityId, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Obtiene las calificaciones del usuario autenticado.
     * GET /api/v1/ratings/my-ratings
     */
    @GetMapping("/my-ratings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyRatings() {
        try {
            logger.info("GET /api/v1/ratings/my-ratings - Fetching my ratings");

            User currentUser = getAuthenticatedUser();
            List<RatingResponse> ratings = ratingService.getMyRatings(currentUser);
            return ResponseEntity.ok(ratings);
            
        } catch (Exception e) {
            logger.error("Error fetching my ratings: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Elimina una calificación.
     * DELETE /api/v1/ratings/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            logger.info("DELETE /api/v1/ratings/{} - Deleting rating", id);

            User currentUser = getAuthenticatedUser();
            ratingService.deleteRating(id, currentUser);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Calificación eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error deleting rating {}: {}", id, e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
