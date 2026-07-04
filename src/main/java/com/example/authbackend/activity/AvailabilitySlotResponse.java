package com.example.authbackend.activity;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilitySlotResponse {
    private LocalDate date;
    private LocalTime time;
    private int availableSlots;
}
