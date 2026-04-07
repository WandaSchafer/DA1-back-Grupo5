package com.example.authbackend.activity;

import com.example.authbackend.user.User;
import com.example.authbackend.user.UserPreference;
import com.example.authbackend.user.UserPreferenceRepository;
import com.example.authbackend.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository,
                          UserPreferenceRepository userPreferenceRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    public List<ActivityListItemResponse> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::mapToListItem)
                .toList();
    }

    public ActivityDetailResponse getActivityById(Long id) {
        Activity a = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        return new ActivityDetailResponse(
                a.getId(),
                a.getName(),
                a.getDescription(),
                a.getDestination(),
                a.getCategory(),
                a.getDuration(),
                a.getPrice(),
                a.getAvailableSlots(),
                a.getImageUrl()
        );
    }

    /**
     * Obtiene actividades recomendadas basadas en las preferencias del usuario autenticado
     */
    public List<ActivityListItemResponse> getRecommendedActivities() {
        User user = getAuthenticatedUser();
        UserPreference preferences = userPreferenceRepository.findByUserId(user.getId()).orElse(null);

        List<Activity> recommendedActivities = new java.util.ArrayList<>();

        // Si existe preferencia por categoría, buscar en esa categoría
        if (preferences != null && preferences.getPreferredCategory() != null && !preferences.getPreferredCategory().isBlank()) {
            recommendedActivities.addAll(
                    activityRepository.findByCategoryIgnoreCase(
                            preferences.getPreferredCategory(),
                            org.springframework.data.domain.PageRequest.of(0, 50)
                    ).getContent()
            );
        }

        // Si no hay preferencias o no se encontraron actividades, intentar con travelPreferences (retrocompatibilidad)
        if (recommendedActivities.isEmpty()) {
            String travelPreferences = user.getTravelPreferences();

            if (travelPreferences != null && !travelPreferences.isBlank()) {
                List<String> preferences_list = Arrays.stream(travelPreferences.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList();

                if (!preferences_list.isEmpty()) {
                    recommendedActivities = activityRepository.findByCategoryIgnoreCaseIn(preferences_list);
                }
            }
        }

        // Si aún no hay actividades, retornar todas
        if (recommendedActivities.isEmpty()) {
            return getAllActivities();
        }

        return recommendedActivities.stream()
                .map(this::mapToListItem)
                .toList();
    }

    /**
     * Obtiene el usuario autenticado desde SecurityContext
     */
    private User getAuthenticatedUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Fallback a getDefaultUser() para compatibilidad
            return getDefaultUser();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElse(getDefaultUser());
    }

    /**
     * Obtiene el primer usuario registrado (fallback)
     */
    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios registrados"));
    }

    private ActivityListItemResponse mapToListItem(Activity a) {
        return new ActivityListItemResponse(
                a.getId(),
                a.getName(),
                a.getDestination(),
                a.getCategory(),
                a.getDuration(),
                a.getPrice(),
                a.getAvailableSlots(),
                a.getImageUrl()
        );
    }
}