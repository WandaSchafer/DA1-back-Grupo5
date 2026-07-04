package com.example.authbackend.activity;

import com.example.authbackend.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityAvailabilityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityAvailabilityService.class);

    private final ActivityAvailabilityRepository availabilityRepository;
    private final ActivityRepository activityRepository;

    public ActivityAvailabilityService(
            ActivityAvailabilityRepository availabilityRepository,
            ActivityRepository activityRepository
    ) {
        this.availabilityRepository = availabilityRepository;
        this.activityRepository = activityRepository;
    }

    public List<AvailabilitySlotResponse> getAvailability(Long activityId) {
        log.info("Buscando disponibilidad para actividad ID: {}", activityId);

        if (!activityRepository.existsById(activityId)) {
            log.warn("Actividad inexistente: ID {}", activityId);
            throw new ResourceNotFoundException("La actividad con ID " + activityId + " no existe");
        }

        List<AvailabilitySlotResponse> slots =
                availabilityRepository.findByActivityIdOrderByDateAscTimeAsc(activityId)
                        .stream()
                        .map(a -> new AvailabilitySlotResponse(
                                a.getDate(),
                                a.getTime(),
                                a.getAvailableSlots()
                        ))
                        .toList();

        log.info("Se encontraron {} slots para actividad ID: {}", slots.size(), activityId);

        return slots;
    }
}