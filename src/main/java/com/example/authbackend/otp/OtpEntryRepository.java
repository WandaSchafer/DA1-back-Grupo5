package com.example.authbackend.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpEntryRepository extends JpaRepository<OtpEntry, Long> {
    Optional<OtpEntry> findByEmailIgnoreCase(String email);
    void deleteByEmailIgnoreCase(String email);
}
