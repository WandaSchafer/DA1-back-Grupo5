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

    public MessageResponse requestOtp(OtpRequest request) {
        User user = findUserByEmail(request.getEmail());
        generateAndStoreOtp(user.getEmail());
        return new MessageResponse("OTP enviado correctamente");
    }

    public MessageResponse resendOtp(OtpRequest request) {
        User user = findUserByEmail(request.getEmail());
        generateAndStoreOtp(user.getEmail());
        return new MessageResponse("Nuevo OTP enviado correctamente");
    }

    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        String email = normalizeEmail(request.getEmail());
        User user = findUserByEmail(email);
        OtpEntry otpEntry = otpStore.findByEmail(email)
                .orElseThrow(() -> new OtpValidationException("No hay un OTP activo para este email"));

        if (otpEntry.getExpiresAt().isBefore(Instant.now())) {
            otpStore.remove(email);
            throw new OtpValidationException("El OTP ha expirado");
        }

        if (!passwordEncoder.matches(request.getOtp(), otpEntry.getHashedOtp())) {
            throw new OtpValidationException("El OTP es invalido");
        }

        otpStore.remove(email);
        return authTokenService.generateAuthResponse(user);
    }

    private void generateAndStoreOtp(String email) {
        String otp = generateOtp();
        Instant expiresAt = Instant.now().plusMillis(otpExpirationMs);
        String hashedOtp = passwordEncoder.encode(otp);

        otpStore.save(email, new OtpEntry(hashedOtp, expiresAt));

        LOGGER.info("Mock OTP email sent to {} with code {}. Expires at {}", email, otp, expiresAt);
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
