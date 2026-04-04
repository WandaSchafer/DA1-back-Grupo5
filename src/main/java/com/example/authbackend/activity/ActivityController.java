package com.example.authbackend.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@CrossOrigin
public class ActivityController {

    private final ActivityRepository activityRepository;
    private final ActivityAvailabilityService activityAvailabilityService;

    public ActivityController(ActivityRepository activityRepository,
                              ActivityAvailabilityService activityAvailabilityService) {
        this.activityRepository = activityRepository;
        this.activityAvailabilityService = activityAvailabilityService;
    }

    @GetMapping
    public Page<Activity> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return activityRepository.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }

    @GetMapping("/{id}/availability")
    public List<AvailabilitySlotResponse> getAvailability(@PathVariable Long id) {
        return activityAvailabilityService.getAvailability(id);
    }
}