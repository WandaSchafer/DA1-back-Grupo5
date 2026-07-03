package com.example.authbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findByUserIdAndActivityIdAndStatus(Long userId, Long activityId, ReservationStatus status);

    boolean existsByUserIdAndActivityIdAndAvailability_DateAndAvailability_TimeAndStatus(
            Long userId,
            Long activityId,
            LocalDate date,
            LocalTime time,
            ReservationStatus status
    );

        @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.user.id = :userId
          AND r.status = com.example.authbackend.reservation.ReservationStatus.FINISHED
          AND (:fromDate IS NULL OR r.availability.date >= :fromDate)
          AND (:toDate IS NULL OR r.availability.date <= :toDate)
          AND (:destination IS NULL OR LOWER(r.activity.destination) = LOWER(:destination))
        ORDER BY r.availability.date DESC, r.availability.time DESC
    """)
    List<Reservation> findFinishedReservationsByFilters(Long userId,
                                                        LocalDate fromDate,
                                                        LocalDate toDate,
                                                        String destination);
}
