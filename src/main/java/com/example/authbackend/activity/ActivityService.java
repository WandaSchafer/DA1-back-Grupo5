package com.example.authbackend.activity;

import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public List<ActivityListItemResponse> getAllActivities() {
        return activityRepository.findAll().stream()
                .map(this::mapToListItem)
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

    public List<ActivityListItemResponse> getRecommendedActivities() {
        User user = getDefaultUser();

        String travelPreferences = user.getTravelPreferences();

        if (travelPreferences == null || travelPreferences.isBlank()) {
            return getAllActivities();
        }

        List<String> preferences = Arrays.stream(travelPreferences.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (preferences.isEmpty()) {
            return getAllActivities();
        }

        List<Activity> matchingActivities = activityRepository.findByCategoryIgnoreCaseIn(preferences);

        if (matchingActivities.isEmpty()) {
            return getAllActivities();
        }

        return matchingActivities.stream()
                .map(this::mapToListItem)
                .toList();
    }

    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios registrados"));
    }

    private ActivityListItemResponse mapToListItem(Activity a) {
        return new ActivityListItemResponse(
                a.getId(),
                a.getName(),
                a.getDestination(),
                a.getCategory(),
                a.getDuration(),
                a.getPrice(),
                a.getAvailableSlots(),
                a.getImageUrl()
        );
    }
}