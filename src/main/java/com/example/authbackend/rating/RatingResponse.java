package com.example.authbackend.rating;

import java.time.LocalDateTime;

public class RatingResponse {

    private Long id;
    private Long userId;
    private String username;
    private String userProfileImageUrl;
    private Long activityId;
    private String activityName;
    private Integer activityScore;
    private Integer guideScore;
    private String comment;
    private LocalDateTime createdAt;

    public RatingResponse() {
    }

    public RatingResponse(Long id, Long userId, String username, String userProfileImageUrl,
                         Long activityId, String activityName, Integer activityScore,
                         Integer guideScore, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.userProfileImageUrl = userProfileImageUrl;
        this.activityId = activityId;
        this.activityName = activityName;
        this.activityScore = activityScore;
        this.guideScore = guideScore;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getActivityScore() {
        return activityScore;
    }

    public void setActivityScore(Integer activityScore) {
        this.activityScore = activityScore;
    }

    public Integer getGuideScore() {
        return guideScore;
    }

    public void setGuideScore(Integer guideScore) {
        this.guideScore = guideScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
