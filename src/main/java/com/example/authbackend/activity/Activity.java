package com.example.authbackend.activity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String destination;
    private String category;
    private String duration;
    private double price;

    @JsonProperty("available_slots")
    @Column(name = "available_slots")
    private int availableSlots;

    @JsonProperty("image_url")
    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "guide_name")
    private String guideName;

    @Column(name = "meeting_point_address")
    private String meetingPointAddress;

    @Column(name = "meeting_point_lat")
    private Double meetingPointLat;

    @Column(name = "meeting_point_lng")
    private Double meetingPointLng;

    public Activity() {}
}