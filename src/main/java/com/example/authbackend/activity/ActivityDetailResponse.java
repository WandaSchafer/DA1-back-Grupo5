package com.example.authbackend.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String destination;
    private String category;
    private String duration;
    private double price;
    private int availableSlots;
    private String imageUrl;
    private String meetingPointAddress;
    private Double meetingPointLat;
    private Double meetingPointLng;
}
