package com.example.authbackend.reservation;

import com.example.authbackend.activity.Activity;
import com.example.authbackend.rating.Rating;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

// COMPLETAR: Faltan campos que necesita el frontend
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationResponse {
    private Long id;
    private Long activityId;
    private String activityName;
    private String date;
    private String time;
    private Integer participants;
    private Double totalPrice;
    private String status;
    private String imageUrl;
    private String meetingPointAddress;

    // Campos de la calificación (si existe)
    private Integer activityScore;
    private Integer guideScore;
    private String ratingComment;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.status = reservation.getStatus().name();
        this.participants = reservation.getParticipants();
        this.totalPrice = Optional.ofNullable(reservation.getTotalPrice()).map(BigDecimal::doubleValue).orElse(0.0);

        if (reservation.getActivityAvailability() != null) {
            Activity activity = reservation.getActivityAvailability().getActivity();
            this.activityId = activity.getId();
            this.activityName = activity.getName();
            this.imageUrl = activity.getImageUrl();
            this.meetingPointAddress = activity.getMeetingPointAddress();

            this.date = reservation.getActivityAvailability().getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
            this.time = reservation.getActivityAvailability().getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMeetingPointAddress() {
        return meetingPointAddress;
    }

    public void setMeetingPointAddress(String meetingPointAddress) {
        this.meetingPointAddress = meetingPointAddress;
    }

    public Integer getActivityScore() {
        return activityScore;
    }

    public void setActivityScore(Integer activityScore) {
        this.activityScore = activityScore;
    }

    public Integer getGuideScore() {
        return guideScore;
    }

    public void setGuideScore(Integer guideScore) {
        this.guideScore = guideScore;
    }

    public String getRatingComment() {
        return ratingComment;
    }

    public void setRatingComment(String ratingComment) {
        this.ratingComment = ratingComment;
    }

    public void addRatingDetails(Rating rating) {
        if (rating != null) {
            this.activityScore = rating.getActivityScore();
            this.guideScore = rating.getGuideScore();
            this.ratingComment = rating.getComment();
        }
    }
}