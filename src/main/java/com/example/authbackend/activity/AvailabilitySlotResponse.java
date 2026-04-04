package com.example.authbackend.activity;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailabilitySlotResponse {

    private LocalDate date;
    private LocalTime time;
    private int availableSlots;

    public AvailabilitySlotResponse(LocalDate date, LocalTime time, int availableSlots) {
        this.date = date;
        this.time = time;
        this.availableSlots = availableSlots;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }
}
