package com.example.authbackend.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    @Column(name = "last_price_change")
    private LocalDateTime lastPriceChange;

    @Column(name = "last_availability_change")
    private LocalDateTime lastAvailabilityChange;

    @Column(name = "price_before_change")
    private Double priceBeforeChange;

    @Column(name = "availability_before_change")
    private Integer availabilityBeforeChange;

    public Activity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGuideName() {
    return guideName;
}

public void setGuideName(String guideName) {
    this.guideName = guideName;
}

public LocalDateTime getLastPriceChange() {
    return lastPriceChange;
}

public void setLastPriceChange(LocalDateTime lastPriceChange) {
    this.lastPriceChange = lastPriceChange;
}

public LocalDateTime getLastAvailabilityChange() {
    return lastAvailabilityChange;
}

public void setLastAvailabilityChange(LocalDateTime lastAvailabilityChange) {
    this.lastAvailabilityChange = lastAvailabilityChange;
}

public Double getPriceBeforeChange() {
    return priceBeforeChange;
}

public void setPriceBeforeChange(Double priceBeforeChange) {
    this.priceBeforeChange = priceBeforeChange;
}

public Integer getAvailabilityBeforeChange() {
    return availabilityBeforeChange;
}

public void setAvailabilityBeforeChange(Integer availabilityBeforeChange) {
    this.availabilityBeforeChange = availabilityBeforeChange;
}
}