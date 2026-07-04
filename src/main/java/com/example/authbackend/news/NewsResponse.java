package com.example.authbackend.news;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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