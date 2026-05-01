package com.example.authbackend.reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public class RescheduleRequest {

    private LocalDate date;
    private LocalTime time;
    private int participants;

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getParticipants() {
        return participants;
    }
}