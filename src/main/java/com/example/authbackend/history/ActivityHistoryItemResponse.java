package com.example.authbackend.history;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityHistoryItemResponse {
    private Long reservationId;
    private Long activityId;
    private LocalDate date;
    private String activityName;
    private String destination;
    private String guideName;
    private String duration;
}
