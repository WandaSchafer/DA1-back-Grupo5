package com.example.authbackend.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationResponse {

    private Long id;

    @JsonProperty("activityId")
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
    

    public ReservationResponse(Long id, String activityName, String destination, String imageUrl,
                                LocalDate date, LocalTime time,
                                int participants, ReservationStatus status,
                                String guideName, String cancellationPolicy, LocalDateTime createdAt,
                                double totalPrice, Long activityId,
                                Integer activityScore, Integer guideScore, String ratingComment) {
        this.id = id;
        this.activityName = activityName;
        this.destination = destination;
        this.imageUrl = imageUrl;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.status = status;
        this.guideName = guideName;
        this.cancellationPolicy = cancellationPolicy;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.activityId = activityId;
        this.activityScore = activityScore;
        this.guideScore = guideScore;
        this.ratingComment = ratingComment;
}

    public Long getId() {
        return id;
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

    public String getGuideName() {
        return guideName;
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

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setActivityId(Long activityId)
    {
        this.activityId = activityId;
    }

    public Integer getActivityScore() {
        return activityScore;
    }

    public Integer getGuideScore() {
        return guideScore;
    }

    public String getRatingComment() {
        return ratingComment;
    }
}