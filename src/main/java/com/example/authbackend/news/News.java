package com.example.authbackend.news;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    
}