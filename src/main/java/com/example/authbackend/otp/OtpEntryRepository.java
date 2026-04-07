package com.example.authbackend.otp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpEntryRepository extends JpaRepository<OtpEntry, Long> {
    Optional<OtpEntry> findByEmailIgnoreCase(String email);
    void deleteByEmailIgnoreCase(String email);
}
