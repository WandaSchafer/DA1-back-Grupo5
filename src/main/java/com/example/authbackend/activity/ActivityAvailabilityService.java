package com.example.authbackend.activity;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityAvailabilityService {

    private final ActivityAvailabilityRepository repository;

    public ActivityAvailabilityService(ActivityAvailabilityRepository repository) {
        this.repository = repository;
    }

    public List<AvailabilitySlotResponse> getAvailability(Long activityId) {
        return repository.findByActivityIdOrderByDateAscTimeAsc(activityId)
                .stream()
                .map(a -> new AvailabilitySlotResponse(
                        a.getDate(),
                        a.getTime(),
                        a.getAvailableSlots()
                ))
                .toList();
    }
}