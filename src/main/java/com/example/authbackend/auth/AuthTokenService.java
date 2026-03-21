package com.example.authbackend.auth;

import com.example.authbackend.auth.dto.AuthResponse;
import com.example.authbackend.security.JwtService;
import com.example.authbackend.security.user.CustomUserDetails;
import com.example.authbackend.user.User;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    private final JwtService jwtService;

    public AuthTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public AuthResponse generateAuthResponse(User user) {
        return generateAuthResponse(new CustomUserDetails(user));
    }

    public AuthResponse generateAuthResponse(CustomUserDetails userDetails) {
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                "Bearer",
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getRole().name()
        );
    }
}
