package com.example.authbackend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    private String cancellationPolicy;
    private LocalDateTime createdAt;
    private double totalPrice;

    public ReservationResponse(Long id, Long activityId, String activityName, String destination, String imageUrl,
                               LocalDate date, LocalTime time,
                               int participants, ReservationStatus status,
                               String cancellationPolicy, LocalDateTime createdAt,
                               double totalPrice) {
        this.id = id;
        this.activityId = activityId;
        this.activityName = activityName;
        this.destination = destination;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.status = status;
        this.cancellationPolicy = cancellationPolicy;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getDestination() {
        return destination;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getParticipants() {
        return participants;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}