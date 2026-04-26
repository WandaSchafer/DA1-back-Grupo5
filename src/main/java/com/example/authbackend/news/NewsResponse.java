package com.example.authbackend.news;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewsResponse {

    private Long id;
    private String title;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("activity_id")
    private Long activityId;

    @JsonProperty("link_type")
    private String linkType;

    @JsonProperty("link_url")
    private String linkUrl;

    public NewsResponse() {}

    public NewsResponse(Long id, String title, String description, String imageUrl, 
                       Long activityId, String linkType, String linkUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.activityId = activityId;
        this.linkType = linkType;
        this.linkUrl = linkUrl;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    // Builder estático para crear desde entidad News
    public static NewsResponse fromEntity(News news) {
        return new NewsResponse(
            news.getId(),
            news.getTitle(),
            news.getDescription(),
            news.getImageUrl(),
            news.getActivityId(),
            news.getLinkType() != null ? news.getLinkType().name() : "ACTIVITY",
            news.getLinkUrl()
        );
    }
}