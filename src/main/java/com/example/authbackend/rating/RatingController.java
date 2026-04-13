package com.example.authbackend.rating;

import com.example.authbackend.security.JwtService;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Crea una nueva calificación para una actividad completada.
     * POST /api/v1/ratings/activity/{activityId}
     */
    @PostMapping("/activity/{activityId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createRating(
            @PathVariable Long activityId,
            @Valid @RequestBody CreateRatingRequest request,
            @RequestHeader("Authorization") String token) {
        
        try {
            logger.info("POST /api/v1/ratings/activity/{} - Creating rating", activityId);
            
            String username = jwtService.extractUsername(token.substring(7));
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
    public ResponseEntity<?> getMyRatings(@RequestHeader("Authorization") String token) {
        try {
            logger.info("GET /api/v1/ratings/my-ratings - Fetching my ratings");
            
            String username = jwtService.extractUsername(token.substring(7));
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
    public ResponseEntity<?> deleteRating(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        try {
            logger.info("DELETE /api/v1/ratings/{} - Deleting rating", id);
            
            String username = jwtService.extractUsername(token.substring(7));
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
