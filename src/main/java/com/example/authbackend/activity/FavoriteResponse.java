package com.example.authbackend.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class FavoriteResponse {

    @JsonProperty("favorite_id")
    private Long favoriteId;

    private Long id;
    private String name;
    private String description;
    private String destination;
    private String category;
    private String duration;
    private Double price;

    @JsonProperty("available_slots")
    private Integer availableSlots;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("price_when_marked")
    private Double priceWhenMarked;

    @JsonProperty("available_slots_when_marked")
    private Integer availableSlotsWhenMarked;

    @JsonProperty("has_price_changed")
    private Boolean hasPriceChanged;

    @JsonProperty("has_availability_changed")
    private Boolean hasAvailabilityChanged;

    @JsonProperty("price_difference")
    private Double priceDifference;

    @JsonProperty("availability_difference")
    private Integer availabilityDifference;

    @JsonProperty("marked_at")
    private LocalDateTime markedAt;

    public FavoriteResponse() {}

    public FavoriteResponse(Favorite favorite) {
        Activity activity = favorite.getActivity();

        this.favoriteId = favorite.getId();
        this.id = activity.getId();
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.destination = activity.getDestination();
        this.category = activity.getCategory();
        this.duration = activity.getDuration();
        this.price = activity.getPrice();
        this.availableSlots = activity.getAvailableSlots();
        this.imageUrl = activity.getImageUrl();

        this.priceWhenMarked = favorite.getPriceWhenMarked();
        this.availableSlotsWhenMarked = favorite.getAvailableSlotsWhenMarked();
        this.hasPriceChanged = favorite.getHasPriceChanged();
        this.hasAvailabilityChanged = favorite.getHasAvailabilityChanged();

        // Calcular diferencias
        this.priceDifference = activity.getPrice() - favorite.getPriceWhenMarked();
        this.availabilityDifference = activity.getAvailableSlots() - favorite.getAvailableSlotsWhenMarked();

        this.markedAt = favorite.getCreatedAt();
    }

    // Getters y Setters
    public Long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Long favoriteId) {
        this.favoriteId = favoriteId;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPriceWhenMarked() {
        return priceWhenMarked;
    }

    public void setPriceWhenMarked(Double priceWhenMarked) {
        this.priceWhenMarked = priceWhenMarked;
    }

    public Integer getAvailableSlotsWhenMarked() {
        return availableSlotsWhenMarked;
    }

    public void setAvailableSlotsWhenMarked(Integer availableSlotsWhenMarked) {
        this.availableSlotsWhenMarked = availableSlotsWhenMarked;
    }

    public Boolean getHasPriceChanged() {
        return hasPriceChanged;
    }

    public void setHasPriceChanged(Boolean hasPriceChanged) {
        this.hasPriceChanged = hasPriceChanged;
    }

    public Boolean getHasAvailabilityChanged() {
        return hasAvailabilityChanged;
    }

    public void setHasAvailabilityChanged(Boolean hasAvailabilityChanged) {
        this.hasAvailabilityChanged = hasAvailabilityChanged;
    }

    public Double getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(Double priceDifference) {
        this.priceDifference = priceDifference;
    }

    public Integer getAvailabilityDifference() {
        return availabilityDifference;
    }

    public void setAvailabilityDifference(Integer availabilityDifference) {
        this.availabilityDifference = availabilityDifference;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }
}
