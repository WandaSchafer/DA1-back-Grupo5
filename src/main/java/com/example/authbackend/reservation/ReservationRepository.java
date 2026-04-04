package com.example.authbackend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndActivityIdAndAvailability_DateAndAvailability_TimeAndStatus(
            Long userId,
            Long activityId,
            LocalDate date,
            LocalTime time,
            ReservationStatus status
    );
}
