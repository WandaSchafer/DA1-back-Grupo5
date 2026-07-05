package com.example.authbackend.reservation;

import lombok.Data;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateResponse {
    private Long reservationId;
    private Long activityId;
    private String activityName;
    private String status;
    private String message;
    private LocalDateTime updatedAt;
}