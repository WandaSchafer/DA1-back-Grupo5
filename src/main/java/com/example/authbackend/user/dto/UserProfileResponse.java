package com.example.authbackend.user.dto;

public class UserProfileResponse {

    private Long id;
    private String username;
    private String email;
    private String phone;
    private String profileImageUrl;
    private String travelPreferences; // Mantener por retrocompatibilidad
    private UserPreferenceResponse preferences;

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

    public UserProfileResponse(Long id, String username, String email, String phone, String profileImageUrl, 
                               String travelPreferences, UserPreferenceResponse preferences) {
        this(id, username, email, phone, profileImageUrl, travelPreferences);
        this.preferences = preferences;
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

    public UserPreferenceResponse getPreferences() {
        return preferences;
    }

    public void setPreferences(UserPreferenceResponse preferences) {
        this.preferences = preferences;
    }
}