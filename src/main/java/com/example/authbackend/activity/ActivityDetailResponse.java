package com.example.authbackend.activity;

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


    public ActivityDetailResponse(Long id, String name, String description,
                              String destination, String category, String duration,
                              double price, int availableSlots, String imageUrl,
                              String meetingPointAddress, Double meetingPointLat, Double meetingPointLng) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.destination = destination;
        this.category = category;
        this.duration = duration;
        this.price = price;
        this.availableSlots = availableSlots;
        this.imageUrl = imageUrl;
        this.meetingPointAddress = meetingPointAddress;
        this.meetingPointLat = meetingPointLat;
        this.meetingPointLng = meetingPointLng;
}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDestination() {
        return destination;
    }

    public String getCategory() {
        return category;
    }

    public String getDuration() {
        return duration;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getMeetingPointAddress() {
        return meetingPointAddress;
    }

    public Double getMeetingPointLat() {
        return meetingPointLat;
    }

    public Double getMeetingPointLng() {
        return meetingPointLng;
    }


}
