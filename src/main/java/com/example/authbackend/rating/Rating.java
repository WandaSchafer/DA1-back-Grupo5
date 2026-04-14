package com.example.authbackend.rating;

import com.example.authbackend.activity.Activity;
import com.example.authbackend.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ratings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "activity_id"})
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "activity_score", nullable = false)
    private Integer activityScore;

    @Column(name = "guide_score", nullable = false)
    private Integer guideScore;

    @Column(name = "comment", columnDefinition = "VARCHAR(300)")
    private String comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Rating() {
    }

    public Rating(User user, Activity activity, Integer activityScore, Integer guideScore, String comment) {
        this.user = user;
        this.activity = activity;
        this.activityScore = activityScore;
        this.guideScore = guideScore;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
