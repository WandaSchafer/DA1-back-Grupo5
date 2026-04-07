package com.example.authbackend.user;

import com.example.authbackend.user.dto.UpdateUserProfileRequest;
import com.example.authbackend.user.dto.UserPreferenceResponse;
import com.example.authbackend.user.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    public UserService(UserRepository userRepository, UserPreferenceRepository userPreferenceRepository) {
        this.userRepository = userRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    public UserProfileResponse getMyProfile() {
        User user = getAuthenticatedUser();
        LOGGER.warn("[PROFILE GET] Obteniendo perfil para usuario: {}", user.getEmail());
        UserPreference preferences = userPreferenceRepository.findByUserId(user.getId()).orElse(null);

        if (preferences != null) {
            LOGGER.info("[PROFILE GET] Preferencias encontradas: categoria={}, destino={}",
                    preferences.getPreferredCategory(),
                    preferences.getPreferredDestination());
        } else {
            LOGGER.info("[PROFILE GET] No hay preferencias personalizadas");
        }

        return mapToProfileResponse(user, preferences);
    }

    @Transactional
    public UserProfileResponse updateMyProfile(UpdateUserProfileRequest request) {
        User user = getAuthenticatedUser();
        LOGGER.warn("[PROFILE UPDATE] Actualizando perfil para usuario: {}", user.getEmail());

        // Actualizar datos básicos del usuario
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
            LOGGER.info("[PROFILE UPDATE] Username actualizado a: {}", request.getUsername());
        }

        user.setPhone(request.getPhone());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setTravelPreferences(request.getTravelPreferences());

        User savedUser = userRepository.save(user);
        LOGGER.warn("[PROFILE UPDATE] ✓ Datos de usuario guardados");

        // Actualizar preferencias si se proporcionan
        UserPreference preferences = null;
        if (request.getPreferences() != null) {
            preferences = userPreferenceRepository.findByUserId(savedUser.getId())
                    .orElse(new UserPreference(savedUser));

            preferences.setUser(savedUser);
            preferences.setPreferredCategory(request.getPreferences().getPreferredCategory());
            preferences.setMaxPrice(request.getPreferences().getMaxPrice());
            preferences.setPreferredDestination(request.getPreferences().getPreferredDestination());
            preferences.setActivityDuration(request.getPreferences().getActivityDuration());

            preferences = userPreferenceRepository.save(preferences);
            LOGGER.warn("[PROFILE UPDATE] ✓ Preferencias guardadas: categoria={}, destino={}, maxPrice={}",
                    preferences.getPreferredCategory(),
                    preferences.getPreferredDestination(),
                    preferences.getMaxPrice());
        } else {
            preferences = userPreferenceRepository.findByUserId(savedUser.getId()).orElse(null);
        }

        LOGGER.warn("[PROFILE UPDATE] ✓ PERFIL ACTUALIZADO EXITOSAMENTE para: {}", user.getEmail());
        return mapToProfileResponse(savedUser, preferences);
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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
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

    private UserProfileResponse mapToProfileResponse(User user, UserPreference preferences) {
        UserPreferenceResponse prefResponse = null;

        if (preferences != null) {
            prefResponse = new UserPreferenceResponse(
                    preferences.getId(),
                    preferences.getPreferredCategory(),
                    preferences.getMaxPrice(),
                    preferences.getPreferredDestination(),
                    preferences.getActivityDuration()
            );
        }

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImageUrl(),
                user.getTravelPreferences(),
                prefResponse
        );
    }
}