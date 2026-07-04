package com.example.authbackend.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class CreateReservationRequest {
    private Long activityId;
    private LocalDate date;
    private LocalTime time;
    private int participants;
}