package com.example.authbackend.otp;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class OtpStore {

    private final ConcurrentHashMap<String, OtpEntry> otpEntries = new ConcurrentHashMap<>();

    public void save(String email, OtpEntry otpEntry) {
        otpEntries.put(email, otpEntry);
    }

    public Optional<OtpEntry> findByEmail(String email) {
        return Optional.ofNullable(otpEntries.get(email));
    }

    public void remove(String email) {
        otpEntries.remove(email);
    }
}
