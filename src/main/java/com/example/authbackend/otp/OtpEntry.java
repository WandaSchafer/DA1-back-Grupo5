package com.example.authbackend.otp;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "otp_entries")
public class OtpEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String hashedOtp;

    @Column(nullable = false)
    private Instant expiresAt;

    public OtpEntry() {}

    public OtpEntry(String email, String hashedOtp, Instant expiresAt) {
        this.email = email;
        this.hashedOtp = hashedOtp;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedOtp() {
        return hashedOtp;
    }

    public void setHashedOtp(String hashedOtp) {
        this.hashedOtp = hashedOtp;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}
