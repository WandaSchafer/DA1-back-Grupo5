package com.example.authbackend.activity;

import com.example.authbackend.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "price_when_marked", nullable = false)
    private Double priceWhenMarked;

    @Column(name = "available_slots_when_marked", nullable = false)
    private Integer availableSlotsWhenMarked;

    @Column(name = "has_price_changed", nullable = false)
    private Boolean hasPriceChanged = false;

    @Column(name = "has_availability_changed", nullable = false)
    private Boolean hasAvailabilityChanged = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        
        // Detectar cambios de precio
        if (activity.getPrice() != null && !activity.getPrice().equals(priceWhenMarked)) {
            hasPriceChanged = true;
        }
        
        // Detectar cambios de disponibilidad
        if (activity.getAvailableSlots() != availableSlotsWhenMarked) {
            hasAvailabilityChanged = true;
        }
    }

    // Constructores
    public Favorite() {}

    public Favorite(User user, Activity activity, Double priceWhenMarked, Integer availableSlotsWhenMarked) {
        this.user = user;
        this.activity = activity;
        this.priceWhenMarked = priceWhenMarked;
        this.availableSlotsWhenMarked = availableSlotsWhenMarked;
        this.hasPriceChanged = false;
        this.hasAvailabilityChanged = false;
    }

    // Getters y Setters
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

    public Double getPriceWhenMarked() {
        return priceWhenMarked;
    }

    public void setPriceWhenMarked(Double priceWhenMarked) {
        this.priceWhenMarked = priceWhenMarked;
    }

    public Integer getAvailableSlotsWhenMarked() {
        return availableSlotsWhenMarked;
    }

    public void setAvailableSlotsWhenMarked(Integer availableSlotsWhenMarked) {
        this.availableSlotsWhenMarked = availableSlotsWhenMarked;
    }

    public Boolean getHasPriceChanged() {
        return hasPriceChanged;
    }

    public void setHasPriceChanged(Boolean hasPriceChanged) {
        this.hasPriceChanged = hasPriceChanged;
    }

    public Boolean getHasAvailabilityChanged() {
        return hasAvailabilityChanged;
    }

    public void setHasAvailabilityChanged(Boolean hasAvailabilityChanged) {
        this.hasAvailabilityChanged = hasAvailabilityChanged;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
