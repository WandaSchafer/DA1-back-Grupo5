package com.example.authbackend.activity;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityAvailabilityService {

    private final ActivityAvailabilityRepository availabilityRepository;
    private final ActivityRepository activityRepository;

    public ActivityAvailabilityService(ActivityAvailabilityRepository availabilityRepository, ActivityRepository activityRepository) {
        this.availabilityRepository = availabilityRepository;
        this.activityRepository = activityRepository;
    }

    public List<AvailabilitySlotResponse> getAvailability(Long activityId) 
    {
        log.info("Buscando disponibilidad para actividad ID: {}", activityId); // 2. Log de entrada

        // 1. Validamos la existencia
        if (!activityRepository.existsById(activityId)) {
            log.warn("Intento de acceso a disponibilidad de actividad inexistente: ID {}", activityId); // 3. Log de advertencia
            throw new ResourceNotFoundException("La actividad con ID " + activityId + " no existe");
        }

        // 2. Buscamos horarios
        List<AvailabilitySlotResponse> slots = availabilityRepository.findByActivityIdOrderByDateAscTimeAsc(activityId)
                .stream()
                .map(a -> new AvailabilitySlotResponse(
                        a.getDate(),
                        a.getTime(),
                        a.getAvailableSlots()
                ))
                .toList();

        log.info("Se encontraron {} slots para la actividad ID: {}", slots.size(), activityId); // 4. Log de éxito
        return slots;
    }

}