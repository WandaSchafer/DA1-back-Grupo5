package com.example.authbackend.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ActivityAvailabilityRepository extends JpaRepository<ActivityAvailability, Long> {

    List<ActivityAvailability> findByActivityIdOrderByDateAscTimeAsc(Long activityId);

    Optional<ActivityAvailability> findByActivityIdAndDateAndTime(Long activityId, LocalDate date, LocalTime time);
}
