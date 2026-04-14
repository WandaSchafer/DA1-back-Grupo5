package com.example.authbackend.rating;

import com.example.authbackend.activity.Activity;
import com.example.authbackend.activity.ActivityRepository;
import com.example.authbackend.exception.BadRequestException;
import com.example.authbackend.exception.ResourceNotFoundException;
import com.example.authbackend.reservation.Reservation;
import com.example.authbackend.reservation.ReservationRepository;
import com.example.authbackend.reservation.ReservationStatus;
import com.example.authbackend.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {

    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);
    private static final int RATING_WINDOW_HOURS = 48;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Crea una calificación para una actividad completada.
     * Solo permite calificaciones dentro de 48 horas después de que la actividad se completa.
     */
    public RatingResponse createRating(Long activityId, User currentUser, CreateRatingRequest request) {
        logger.info("Creating rating for activity {} by user {}", activityId, currentUser.getId());

        // Verificar que la actividad existe
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + activityId));

        // Verificar que el usuario completó una reservación para esta actividad
        List<Reservation> completedReservations = reservationRepository
                .findByUserIdAndActivityIdAndStatus(currentUser.getId(), activityId, ReservationStatus.COMPLETED);

        if (completedReservations.isEmpty()) {
            logger.warn("User {} attempted to rate activity {} without completing it", currentUser.getId(), activityId);
            throw new BadRequestException("No has completado una reservación para esta actividad");
        }

        // Validar que la calificación esté dentro de las 48 horas
        Reservation latestReservation = completedReservations.get(0);
        LocalDateTime completionTime = latestReservation.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        
        if (completionTime != null) {
            long hoursElapsed = java.time.temporal.ChronoUnit.HOURS.between(completionTime, now);
            if (hoursElapsed > RATING_WINDOW_HOURS) {
                logger.warn("User {} tried to rate activity {} outside 48-hour window ({}h elapsed)", 
                        currentUser.getId(), activityId, hoursElapsed);
                throw new BadRequestException(
                        "Solo puedes calificar dentro de 48 horas después de completar la actividad");
            }
        }

        // Verificar que no existe ya una calificación
        if (ratingRepository.findByUserIdAndActivityId(currentUser.getId(), activityId).isPresent()) {
            logger.warn("User {} attempted duplicate rating for activity {}", currentUser.getId(), activityId);
            throw new BadRequestException("Ya has calificado esta actividad");
        }

        // Crear la calificación
        Rating rating = new Rating();
        rating.setUser(currentUser);
        rating.setActivity(activity);
        rating.setActivityScore(request.getActivityScore());
        rating.setGuideScore(request.getGuideScore());
        rating.setComment(request.getComment());
        rating.setCreatedAt(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);
        logger.info("Rating created successfully with ID {}", savedRating.getId());

        return mapToResponse(savedRating);
    }

    /**
     * Obtiene todas las calificaciones de una actividad.
     */
    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByActivity(Long activityId) {
        logger.info("Fetching ratings for activity {}", activityId);
        
        // Verificar que la actividad existe
        activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + activityId));

        List<Rating> ratings = ratingRepository.findByActivityId(activityId);
        return ratings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene estadísticas de calificación para una actividad.
     */
    @Transactional(readOnly = true)
    public RatingStatsResponse getRatingStats(Long activityId) {
        logger.info("Fetching rating stats for activity {}", activityId);
        
        // Verificar que la actividad existe
        activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con ID: " + activityId));

        Double avgActivityScore = ratingRepository.getAverageActivityScore(activityId);
        Double avgGuideScore = ratingRepository.getAverageGuideScore(activityId);
        Long totalRatings = ratingRepository.countByActivityId(activityId);

        return new RatingStatsResponse(
                avgActivityScore != null ? Math.round(avgActivityScore * 100.0) / 100.0 : null,
                avgGuideScore != null ? Math.round(avgGuideScore * 100.0) / 100.0 : null,
                totalRatings
        );
    }

    /**
     * Obtiene las calificaciones del usuario actual.
     */
    @Transactional(readOnly = true)
    public List<RatingResponse> getMyRatings(User currentUser) {
        logger.info("Fetching ratings for user {}", currentUser.getId());
        
        List<Rating> ratings = ratingRepository.findByUserId(currentUser.getId());
        return ratings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una calificación. Solo el autor puede eliminar su propia calificación.
     */
    public void deleteRating(Long ratingId, User currentUser) {
        logger.info("Deleting rating {} by user {}", ratingId, currentUser.getId());
        
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación no encontrada con ID: " + ratingId));

        // Verificar que el usuario es el autor
        if (!rating.getUser().getId().equals(currentUser.getId())) {
            logger.warn("User {} attempted to delete rating {} owned by user {}", 
                    currentUser.getId(), ratingId, rating.getUser().getId());
            throw new BadRequestException("No puedes eliminar una calificación que no es tuya");
        }

        ratingRepository.deleteById(ratingId);
        logger.info("Rating {} deleted successfully", ratingId);
    }

    /**
     * Mapea una entidad Rating a RatingResponse.
     */
    private RatingResponse mapToResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getUser().getId(),
                rating.getUser().getUsername(),
                rating.getUser().getProfileImageUrl(),
                rating.getActivity().getId(),
                rating.getActivity().getName(),
                rating.getActivityScore(),
                rating.getGuideScore(),
                rating.getComment(),
                rating.getCreatedAt()
        );
    }
}
