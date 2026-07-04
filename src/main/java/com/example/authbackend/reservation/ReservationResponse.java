package com.example.authbackend.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor 
public class ReservationResponse {
    private Long id;
    private Long activityId;
    private String activityName;
    private String destination;
    private String imageUrl;
    private LocalDate date;
    private LocalTime time;
    private int participants;
    private ReservationStatus status;
    private String guideName;
    private String cancellationPolicy;
    private LocalDateTime createdAt;
    private double totalPrice;
    private Integer activityScore;
    private Integer guideScore;
    private String ratingComment;
}