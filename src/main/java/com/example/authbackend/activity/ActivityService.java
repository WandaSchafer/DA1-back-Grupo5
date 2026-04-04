package com.example.authbackend.activity;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityListItemResponse> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(a -> new ActivityListItemResponse(
                        a.getId(),
                        a.getName(),
                        a.getDestination(),
                        a.getCategory(),
                        a.getDuration(),
                        a.getPrice(),
                        a.getAvailableSlots(),
                        a.getImageUrl()
                ))
                .toList();
    }

    public ActivityDetailResponse getActivityById(Long id) {
        Activity a = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrada"));

        return new ActivityDetailResponse(
                a.getId(),
                a.getName(),
                a.getDescription(),
                a.getDestination(),
                a.getCategory(),
                a.getDuration(),
                a.getPrice(),
                a.getAvailableSlots(),
                a.getImageUrl()
        );
    }
}