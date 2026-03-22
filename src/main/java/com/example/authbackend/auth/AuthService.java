package com.example.authbackend.auth;

import com.example.authbackend.auth.dto.AuthResponse;
import com.example.authbackend.auth.dto.LoginRequest;
import com.example.authbackend.auth.dto.RegisterRequest;
import com.example.authbackend.exception.BadRequestException;
import com.example.authbackend.exception.InvalidCredentialsException;
import com.example.authbackend.exception.ResourceConflictException;
import com.example.authbackend.security.user.CustomUserDetails;
import com.example.authbackend.user.Role;
import com.example.authbackend.user.User;
import com.example.authbackend.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authTokenService = authTokenService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();
        Role requestedRole = request.getRole();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("El username ya esta en uso");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("El email ya esta registrado");
        }

        if (requestedRole == Role.ADMIN) {
            throw new BadRequestException("No se permite registrar usuarios ADMIN desde este endpoint");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(requestedRole != null ? requestedRole : Role.USER);

        User savedUser = userRepository.save(user);
        return authTokenService.generateAuthResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        String principal = request.getUsernameOrEmail().contains("@")
                ? request.getUsernameOrEmail().trim().toLowerCase()
                : request.getUsernameOrEmail().trim();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        principal,
                        request.getPassword()
                )
        );

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new InvalidCredentialsException("Credenciales invalidas");
        }

        return authTokenService.generateAuthResponse(userDetails);
    }
}
