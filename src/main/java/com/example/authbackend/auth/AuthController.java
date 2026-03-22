package com.example.authbackend.auth;

import com.example.authbackend.auth.dto.AuthResponse;
import com.example.authbackend.auth.dto.LoginRequest;
import com.example.authbackend.auth.dto.MessageResponse;
import com.example.authbackend.auth.dto.OtpRequest;
import com.example.authbackend.auth.dto.OtpVerifyRequest;
import com.example.authbackend.auth.dto.RegisterRequest;
import com.example.authbackend.otp.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/otp/request")
    public ResponseEntity<MessageResponse> requestOtp(@Valid @RequestBody OtpRequest request) {
        return ResponseEntity.ok(otpService.requestOtp(request));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return ResponseEntity.ok(otpService.verifyOtp(request));
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<MessageResponse> resendOtp(@Valid @RequestBody OtpRequest request) {
        return ResponseEntity.ok(otpService.resendOtp(request));
    }
}
