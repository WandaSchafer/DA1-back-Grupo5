package com.example.authbackend.otp;

import jakarta.persistence.*;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public OtpEntry(String email, String hashedOtp, Instant expiresAt) {
        this.email = email;
        this.hashedOtp = hashedOtp;
        this.expiresAt = expiresAt;
    }
}
