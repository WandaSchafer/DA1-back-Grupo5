package com.example.authbackend.history;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@CrossOrigin
public class ActivityHistoryController {

    private final ActivityHistoryService activityHistoryService;

    public ActivityHistoryController(ActivityHistoryService activityHistoryService) {
        this.activityHistoryService = activityHistoryService;
    }

    @GetMapping
    public List<ActivityHistoryItemResponse> getHistory(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false)
            String destination
    ) {
        return activityHistoryService.getHistory(fromDate, toDate, destination);
    }
}