package com.example.authbackend.reservation;

import com.example.authbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // AGREGADO: Para que compile y funcione el listado 'api/v1/reservations/me'
    List<Reservation> findByUser(User user);

    // 1. Corregido para usar reservationDate según tu entidad real
    List<Reservation> findByUserIdOrderByReservationDateDesc(Long userId);

    // 2. Corregido de forma explícita apuntando al ID de la actividad dentro de la disponibilidad
    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.user.id = :userId 
          AND r.activityAvailability.activity.id = :activityId 
          AND r.status = :status
    """)
    List<Reservation> findByUserIdAndActivityIdAndStatus(@Param("userId") Long userId, 
                                                         @Param("activityId") Long activityId, 
                                                         @Param("status") ReservationStatus status);

    // 3. Corregido de forma explícita mapeando todo el árbol de relaciones reales
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END 
        FROM Reservation r 
        WHERE r.user.id = :userId 
          AND r.activityAvailability.activity.id = :activityId 
          AND r.activityAvailability.date = :date 
          AND r.activityAvailability.time = :time 
          AND r.status = :status
    """)
    boolean existsByUserIdAndActivityIdAndActivityAvailability_DateAndActivityAvailability_TimeAndStatus(
            @Param("userId") Long userId,
            @Param("activityId") Long activityId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time,
            @Param("status") ReservationStatus status
    );

    // 4. Tu filtro de historial que ya estaba corregido
    @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.user.id = :userId
          AND r.status = com.example.authbackend.reservation.ReservationStatus.FINISHED
          AND (:fromDate IS NULL OR r.activityAvailability.date >= :fromDate)
          AND (:toDate IS NULL OR r.activityAvailability.date <= :toDate)
          AND (:destination IS NULL OR LOWER(r.activityAvailability.activity.destination) = LOWER(:destination))
        ORDER BY r.activityAvailability.date DESC, r.activityAvailability.time DESC
    """)
    List<Reservation> findFinishedReservationsByFilters(@Param("userId") Long userId,
                                                        @Param("fromDate") LocalDate fromDate,
                                                        @Param("toDate") LocalDate toDate,
                                                        @Param("destination") String destination);
}