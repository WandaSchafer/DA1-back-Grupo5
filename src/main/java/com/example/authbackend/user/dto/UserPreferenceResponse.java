package com.example.authbackend.user.dto;

public class UserPreferenceResponse {

    private Long id;
    private String preferredCategory;
    private Double maxPrice;
    private String preferredDestination;
    private String activityDuration;

    public UserPreferenceResponse() {}

    public UserPreferenceResponse(Long id, String preferredCategory, Double maxPrice,
                                  String preferredDestination, String activityDuration) {
        this.id = id;
        this.preferredCategory = preferredCategory;
        this.maxPrice = maxPrice;
        this.preferredDestination = preferredDestination;
        this.activityDuration = activityDuration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreferredCategory() {
        return preferredCategory;
    }

    public void setPreferredCategory(String preferredCategory) {
        this.preferredCategory = preferredCategory;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getPreferredDestination() {
        return preferredDestination;
    }

    public void setPreferredDestination(String preferredDestination) {
        this.preferredDestination = preferredDestination;
    }

    public String getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(String activityDuration) {
        this.activityDuration = activityDuration;
    }
}
