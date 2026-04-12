package com.example.authbackend.history;


import java.time.LocalDate;

public class ActivityHistoryItemResponse {

    private Long reservationId;
    private Long activityId;
    private LocalDate date;
    private String activityName;
    private String destination;
    private String guideName;
    private String duration;

    public ActivityHistoryItemResponse(Long reservationId,
                                       Long activityId,
                                       LocalDate date,
                                       String activityName,
                                       String destination,
                                       String guideName,
                                       String duration) {
        this.reservationId = reservationId;
        this.activityId = activityId;
        this.date = date;
        this.activityName = activityName;
        this.destination = destination;
        this.guideName = guideName;
        this.duration = duration;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDestination() {
        return destination;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getDuration() {
        return duration;
    }
}
