package com.example.authbackend.history;

import com.example.authbackend.security.user.CustomUserDetails;
import com.example.authbackend.user.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            String destination,

            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User currentUser = userDetails.getUser();

        return activityHistoryService.getHistory(currentUser, fromDate, toDate, destination);
    }
}