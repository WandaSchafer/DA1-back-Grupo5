package com.example.authbackend.activity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "activity_availabilities")
@Data
public class ActivityAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private int totalSlots;

    @Column(nullable = false)
    private int reservedSlots = 0;

    public int getAvailableSlots() {
        return totalSlots - reservedSlots;
    }
}