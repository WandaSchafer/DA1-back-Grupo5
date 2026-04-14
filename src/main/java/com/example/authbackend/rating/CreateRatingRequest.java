package com.example.authbackend.rating;

import jakarta.validation.constraints.*;

public class CreateRatingRequest {

    @NotNull(message = "activityScore es requerido")
    @Min(value = 1, message = "activityScore debe ser entre 1 y 5")
    @Max(value = 5, message = "activityScore debe ser entre 1 y 5")
    private Integer activityScore;

    @NotNull(message = "guideScore es requerido")
    @Min(value = 1, message = "guideScore debe ser entre 1 y 5")
    @Max(value = 5, message = "guideScore debe ser entre 1 y 5")
    private Integer guideScore;

    @Size(max = 300, message = "El comentario no puede exceder 300 caracteres")
    private String comment;

    public CreateRatingRequest() {
    }

    public CreateRatingRequest(Integer activityScore, Integer guideScore, String comment) {
        this.activityScore = activityScore;
        this.guideScore = guideScore;
        this.comment = comment;
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
}
