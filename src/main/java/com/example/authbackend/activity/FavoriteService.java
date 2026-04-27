package com.example.authbackend.activity;

import com.example.authbackend.exception.ResourceNotFoundException;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                         ActivityRepository activityRepository,
                         UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    /**
     * Obtener usuario autenticado del contexto de seguridad
     */
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
    }

    /**
     * Marcar una actividad como favorita
     */
    public FavoriteResponse addFavorite(Long activityId) {
        User user = getAuthenticatedUser();
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada"));

        // Verificar si ya está marcado como favorito
        if (favoriteRepository.existsByUserIdAndActivityId(user.getId(), activityId)) {
            throw new RuntimeException("Esta actividad ya está marcada como favorita");
        }

        // Crear nuevo favorito
        Favorite favorite = new Favorite(
                user,
                activity,
                activity.getPrice(),
                (double) activity.getAvailableSlots()
        );

        Favorite saved = favoriteRepository.save(favorite);
        return new FavoriteResponse(saved);
    }

    /**
     * Remover una actividad de favoritos
     */
    public void removeFavorite(Long activityId) {
        User user = getAuthenticatedUser();

        Favorite favorite = favoriteRepository.findByUserIdAndActivityId(user.getId(), activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito no encontrado"));

        favoriteRepository.delete(favorite);
    }

    /**
     * Obtener todos los favoritos del usuario autenticado
     */
    public List<FavoriteResponse> getUserFavorites() {
        User user = getAuthenticatedUser();
        return favoriteRepository.findByUserId(user.getId())
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtener favoritos con indicador de cambio de precio
     */
    public List<FavoriteResponse> getFavoritesWithPriceChanges() {
        User user = getAuthenticatedUser();
        return favoriteRepository.findByUserIdAndHasPriceChangedTrue(user.getId())
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtener favoritos con indicador de cambio de disponibilidad
     */
    public List<FavoriteResponse> getFavoritesWithAvailabilityChanges() {
        User user = getAuthenticatedUser();
        return favoriteRepository.findByUserIdAndHasAvailabilityChangedTrue(user.getId())
                .stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtener favoritos con cualquier cambio
     */
    public List<FavoriteResponse> getFavoritesWithChanges() {
        User user = getAuthenticatedUser();
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());
        
        return favorites.stream()
                .filter(f -> f.getHasPriceChanged() || f.getHasAvailabilityChanged())
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * Verificar si una actividad está marcada como favorita
     */
    public boolean isFavorite(Long activityId) {
        User user = getAuthenticatedUser();
        return favoriteRepository.existsByUserIdAndActivityId(user.getId(), activityId);
    }

    /**
     * Marcar cambio de precio en un favorito
     */
    public void markPriceChange(Long activityId, Double oldPrice, Double newPrice) {
        User user = getAuthenticatedUser();
        
        favoriteRepository.findByUserIdAndActivityId(user.getId(), activityId)
                .ifPresent(favorite -> {
                    if (!favorite.getPriceWhenMarked().equals(newPrice)) {
                        favorite.setHasPriceChanged(true);
                        favorite.setUpdatedAt(LocalDateTime.now());
                        favoriteRepository.save(favorite);
                    }
                });
    }

    /**
     * Marcar cambio de disponibilidad en un favorito
     */
    public void markAvailabilityChange(Long activityId, Integer oldSlots, Integer newSlots) {
        User user = getAuthenticatedUser();
        
        favoriteRepository.findByUserIdAndActivityId(user.getId(), activityId)
                .ifPresent(favorite -> {
                    if (!favorite.getAvailableSlotsWhenMarked().equals(newSlots)) {
                        favorite.setHasAvailabilityChanged(true);
                        favorite.setUpdatedAt(LocalDateTime.now());
                        favoriteRepository.save(favorite);
                    }
                });
    }

    /**
     * Limpiar indicador de cambio de precio
     */
    public void clearPriceChangeIndicator(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito no encontrado"));

        favorite.setHasPriceChanged(false);
        favorite.setUpdatedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);
    }

    /**
     * Limpiar indicador de cambio de disponibilidad
     */
    public void clearAvailabilityChangeIndicator(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorito no encontrado"));

        favorite.setHasAvailabilityChanged(false);
        favorite.setUpdatedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);
    }
}
