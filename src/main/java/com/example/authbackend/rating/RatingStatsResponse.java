package com.example.authbackend.rating;

public class RatingStatsResponse {

    private Double averageActivityScore;
    private Double averageGuideScore;
    private Long totalRatings;

    public RatingStatsResponse(Double averageActivityScore, Double averageGuideScore, Long totalRatings) {
        this.averageActivityScore = averageActivityScore;
        this.averageGuideScore = averageGuideScore;
        this.totalRatings = totalRatings;
    }

    public Double getAverageActivityScore() {
        return averageActivityScore;
    }

    public void setAverageActivityScore(Double averageActivityScore) {
        this.averageActivityScore = averageActivityScore;
    }

    public Double getAverageGuideScore() {
        return averageGuideScore;
    }

    public void setAverageGuideScore(Double averageGuideScore) {
        this.averageGuideScore = averageGuideScore;
    }

    public Long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Long totalRatings) {
        this.totalRatings = totalRatings;
    }
}
