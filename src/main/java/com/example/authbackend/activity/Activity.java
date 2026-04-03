package com.example.authbackend.activity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String city;
    private String country;
    private String day;

    private LocalDate date;
    private LocalTime time;

    private Integer capacity;
    private Integer spotsLeft;

    private String imageUrl;

    // getters y setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getDay() { return day; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public Integer getCapacity() { return capacity; }
    public Integer getSpotsLeft() { return spotsLeft; }
    public String getImageUrl() { return imageUrl; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setDay(String day) { this.day = day; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setSpotsLeft(Integer spotsLeft) { this.spotsLeft = spotsLeft; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
