package com.example.authbackend.activity;


import com.example.authbackend.activity.ActivityDetailResponse;
import com.example.authbackend.activity.ActivityListItemResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@CrossOrigin
public class ActivityController {

    private final ActivityService service;

    public ActivityController(ActivityService service) {
        this.service = service;
    }

    @GetMapping
    public List<ActivityListItemResponse> getAll() {
        return service.getAllActivities();
    }

    @GetMapping("/{id}")
    public ActivityDetailResponse getById(@PathVariable Long id) {
        return service.getActivityById(id);
    }
}