package com.example.authbackend.user.dto;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String travelPreferences;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, String username, String email, String phone, String profileImageUrl, String travelPreferences) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.travelPreferences = travelPreferences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getTravelPreferences() {
        return travelPreferences;
    }

    public void setTravelPreferences(String travelPreferences) {
        this.travelPreferences = travelPreferences;
    }
}