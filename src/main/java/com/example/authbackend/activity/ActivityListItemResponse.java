package com.example.authbackend.activity;

public class ActivityListItemResponse {

    private Long id;
    private String name;
    private String city;
    private String country;
    private String day;
    private String date;
    private String time;
    private Integer spotsLeft;
    private String imageUrl;

    public ActivityListItemResponse(Long id, String name, String city, String country,
                                    String day, String date, String time,
                                    Integer spotsLeft, String imageUrl) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.day = day;
        this.date = date;
        this.time = time;
        this.spotsLeft = spotsLeft;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getDay() { return day; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public Integer getSpotsLeft() { return spotsLeft; }
    public String getImageUrl() { return imageUrl; }
}
