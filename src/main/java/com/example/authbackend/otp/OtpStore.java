package com.example.authbackend.otp;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class OtpStore {

    private final OtpEntryRepository otpEntryRepository;
    private final EntityManager entityManager;

    public OtpStore(OtpEntryRepository otpEntryRepository, EntityManager entityManager) {
        this.otpEntryRepository = otpEntryRepository;
        this.entityManager = entityManager;
    }

    public void save(String email, OtpEntry otpEntry) {
        // Eliminar entrada anterior si existe
        otpEntryRepository.deleteByEmailIgnoreCase(email);
        // IMPORTANTE: Forzar que el DELETE se ejecute en BD ANTES del INSERT
        entityManager.flush();
        
        // Guardar nueva
        otpEntry.setEmail(email);
        otpEntryRepository.save(otpEntry);
    }

    public Optional<OtpEntry> findByEmail(String email) {
        return otpEntryRepository.findByEmailIgnoreCase(email);
    }

    public void remove(String email) {
        otpEntryRepository.deleteByEmailIgnoreCase(email);
        // Forzar sincronización con BD
        entityManager.flush();
    }
}
