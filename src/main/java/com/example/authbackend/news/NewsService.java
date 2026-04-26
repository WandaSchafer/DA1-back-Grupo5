package com.example.authbackend.news;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsResponse> getAllNews() {
        return newsRepository.findAllByOrderByIdDesc()
                .stream()
                .map(NewsResponse::fromEntity)
                .collect(Collectors.toList());
    }
}