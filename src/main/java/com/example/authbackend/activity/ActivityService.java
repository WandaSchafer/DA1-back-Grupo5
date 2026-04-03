package com.example.authbackend.activity;

import com.example.authbackend.activity.ActivityListItemResponse;
import com.example.authbackend.activity.ActivityDetailResponse;
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
                        a.getCity(),
                        a.getCountry(),
                        a.getDay(),
                        a.getDate() != null ? a.getDate().toString() : null,
                        a.getTime() != null ? a.getTime().toString() : null,
                        a.getSpotsLeft(),
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
                a.getCity(),
                a.getCountry(),
                a.getDay(),
                a.getDate() != null ? a.getDate().toString() : null,
                a.getTime() != null ? a.getTime().toString() : null,
                a.getCapacity(),
                a.getSpotsLeft(),
                a.getImageUrl()
        );
    }
}