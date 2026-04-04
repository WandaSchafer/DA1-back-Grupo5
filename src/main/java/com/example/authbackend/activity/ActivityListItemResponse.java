package com.example.authbackend.activity;
public class ActivityListItemResponse {

    private Long id;
    private String name;
    private String destination;
    private String category;
    private String duration;
    private double price;
    private int availableSlots;
    private String imageUrl;

    public ActivityListItemResponse(Long id, String name, String destination, String category,
                                    String duration, double price, int availableSlots, String imageUrl) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.category = category;
        this.duration = duration;
        this.price = price;
        this.availableSlots = availableSlots;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
}