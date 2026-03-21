package com.example.authbackend.otp;

import java.time.Instant;

public class OtpEntry {

    private final String hashedOtp;
    private final Instant expiresAt;

    public OtpEntry(String hashedOtp, Instant expiresAt) {
        this.hashedOtp = hashedOtp;
        this.expiresAt = expiresAt;
    }

    public String getHashedOtp() {
        return hashedOtp;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
