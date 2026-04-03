package com.example.authbackend.activity;

public class ActivityDetailResponse {

    private Long id;
    private String name;
    private String description;
    private String city;
    private String country;
    private String day;
    private String date;
    private String time;
    private Integer capacity;
    private Integer spotsLeft;
    private String imageUrl;

    public ActivityDetailResponse(Long id, String name, String description,
                                  String city, String country, String day,
                                  String date, String time,
                                  Integer capacity, Integer spotsLeft,
                                  String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.country = country;
        this.day = day;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.spotsLeft = spotsLeft;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getDay() { return day; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public Integer getCapacity() { return capacity; }
    public Integer getSpotsLeft() { return spotsLeft; }
    public String getImageUrl() { return imageUrl; }
}
