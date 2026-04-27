package com.example.authbackend.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * Buscar todos los favoritos de un usuario
     */
    List<Favorite> findByUserId(Long userId);

    /**
     * Buscar un favorito específico de un usuario
     */
    Optional<Favorite> findByUserIdAndActivityId(Long userId, Long activityId);

    /**
     * Verificar si una actividad está marcada como favorita por un usuario
     */
    boolean existsByUserIdAndActivityId(Long userId, Long activityId);

    /**
     * Obtener favoritos con cambios de precio o disponibilidad
     */
    List<Favorite> findByUserIdAndHasPriceChangedTrue(Long userId);

    List<Favorite> findByUserIdAndHasAvailabilityChangedTrue(Long userId);

    /**
     * Obtener favoritos con cualquier cambio de novedad
     */
    List<Favorite> findByUserIdAndHasPriceChangedTrueOrHasAvailabilityChangedTrue(Long userId);

}
