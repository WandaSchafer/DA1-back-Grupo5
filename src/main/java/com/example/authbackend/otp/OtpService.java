package com.example.authbackend.otp;

import com.example.authbackend.auth.AuthTokenService;
import com.example.authbackend.auth.dto.AuthResponse;
import com.example.authbackend.auth.dto.MessageResponse;
import com.example.authbackend.auth.dto.OtpRequest;
import com.example.authbackend.auth.dto.OtpVerifyRequest;
import com.example.authbackend.exception.OtpValidationException;
import com.example.authbackend.exception.ResourceNotFoundException;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import java.security.SecureRandom;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtpService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenService authTokenService;
    private final OtpStore otpStore;
    private final long otpExpirationMs;

    public OtpService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      AuthTokenService authTokenService,
                      OtpStore otpStore,
                      @Value("${app.otp.expiration-ms:300000}") long otpExpirationMs) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authTokenService = authTokenService;
        this.otpStore = otpStore;
        this.otpExpirationMs = otpExpirationMs;
    }

    @Transactional
    public MessageResponse requestOtp(OtpRequest request) {
        LOGGER.warn("[OTP REQUEST] Solicitando OTP para email: {}", request.getEmail());
        User user = findUserByEmail(request.getEmail());
        generateAndStoreOtp(user.getEmail());
        LOGGER.warn("[OTP REQUEST] OTP generado y guardado exitosamente");
        return new MessageResponse("OTP enviado correctamente");
    }

    @Transactional
    public MessageResponse resendOtp(OtpRequest request) {
        LOGGER.warn("[OTP RESEND] Reenviando OTP para email: {}", request.getEmail());
        User user = findUserByEmail(request.getEmail());
        generateAndStoreOtp(user.getEmail());
        LOGGER.warn("[OTP RESEND] Nuevo OTP generado y guardado exitosamente");
        return new MessageResponse("Nuevo OTP enviado correctamente");
    }

    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        String email = normalizeEmail(request.getEmail());
        LOGGER.warn("\n[OTP VERIFY] Intentando verificar OTP para: {}", email);
        
        User user = findUserByEmail(email);
        OtpEntry otpEntry = otpStore.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.warn("[OTP VERIFY] ✗ FALLO: No existe OTP para: {}", email);
                    return new OtpValidationException("No hay un OTP activo para este email");
                });

        LOGGER.warn("[OTP VERIFY] ✓ OTP encontrado en BD para: {}", email);
        LOGGER.warn("[OTP VERIFY] Expira a: {} (Ahora es: {})", otpEntry.getExpiresAt(), Instant.now());
        
        if (otpEntry.getExpiresAt().isBefore(Instant.now())) {
            LOGGER.warn("[OTP VERIFY] ✗ FALLO: OTP EXPIRADO para: {}", email);
            otpStore.remove(email);
            throw new OtpValidationException("El OTP ha expirado");
        }

        LOGGER.warn("[OTP VERIFY] Código recibido: {}. Comparando con hash BD...", request.getOtp());
        
        if (!passwordEncoder.matches(request.getOtp(), otpEntry.getHashedOtp())) {
            LOGGER.warn("[OTP VERIFY] ✗ FALLO: CÓDIGO INCORRECTO para: {}", email);
            throw new OtpValidationException("El OTP es invalido");
        }

        LOGGER.warn("[OTP VERIFY] ✓ EXITO: CÓDIGO CORRECTO Y VÁLIDO para: {}. Generando token...", email);
        otpStore.remove(email);
        return authTokenService.generateAuthResponse(user);
    }

    private void generateAndStoreOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusMillis(otpExpirationMs);
        String hashedOtp = passwordEncoder.encode(otp);

        LOGGER.warn("\n\n========================================");
        LOGGER.warn("OTP GENERADO PARA: {}", normalizedEmail);
        LOGGER.warn("CÓDIGO OTP: {}  <-- COPIA ESTE CÓDIGO", otp);
        LOGGER.warn("Válido por 5 minutos (hasta las {})", expiresAt);
        LOGGER.warn("========================================\n");

        try {
            otpStore.save(normalizedEmail, new OtpEntry(normalizedEmail, hashedOtp, expiresAt));
            LOGGER.warn("✓ OTP GUARDADO CORRECTAMENTE en BD para: {}", normalizedEmail);
        } catch (Exception e) {
            LOGGER.error("✗ ERROR al guardar OTP en BD: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo guardar el OTP", e);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ese email"));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String generateOtp() {
        int otpNumber = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(otpNumber);
    }
}
