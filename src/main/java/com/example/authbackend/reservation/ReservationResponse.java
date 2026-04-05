package com.example.authbackend.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservationResponse {

    private Long id;
    private String activityName;
    private LocalDate date;
    private LocalTime time;
    private int participants;
    private ReservationStatus status;
    private String cancellationPolicy;
    private LocalDateTime createdAt;
<<<<<<< HEAD
=======
    private double totalPrice;
>>>>>>> 497e323 (Add reservation totalPrice and update DB password)

    public ReservationResponse(Long id, String activityName,
                               LocalDate date, LocalTime time,
                               int participants, ReservationStatus status,
<<<<<<< HEAD
                               String cancellationPolicy, LocalDateTime createdAt) {
=======
                               String cancellationPolicy, LocalDateTime createdAt,
                               double totalPrice) {
>>>>>>> 497e323 (Add reservation totalPrice and update DB password)
        this.id = id;
        this.activityName = activityName;
        this.date = date;
        this.time = time;
        this.participants = participants;
        this.status = status;
        this.cancellationPolicy = cancellationPolicy;
        this.createdAt = createdAt;
<<<<<<< HEAD
=======
        this.totalPrice = totalPrice;
>>>>>>> 497e323 (Add reservation totalPrice and update DB password)
    }

    public Long getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
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
<<<<<<< HEAD
=======

    public double getTotalPrice() {
        return totalPrice;
    }
>>>>>>> 497e323 (Add reservation totalPrice and update DB password)
}