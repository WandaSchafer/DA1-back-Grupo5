package com.example.authbackend.news;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @JsonProperty("image_url")
    @Column(name = "image_url")
    private String imageUrl;

    @NotNull
    @JsonProperty("activity_id")
    @Column(name = "activity_id")
    private Long activityId;

    // Tipo de redirección: ACTIVITY (actividad interna) o EXTERNAL (página externa)
    @Enumerated(EnumType.STRING)
    @Column(name = "link_type")
    private LinkType linkType = LinkType.ACTIVITY;

    public enum LinkType {
        ACTIVITY,
        EXTERNAL
    }

    // URL externa (solo usada cuando linkType = EXTERNAL)
    @JsonProperty("link_url")
    @Column(name = "link_url")
    private String linkUrl;

    public News() {}

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

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}