package com.example.authbackend.user;

import com.example.authbackend.user.dto.UpdateUserProfileRequest;
import com.example.authbackend.user.dto.UserProfileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        try {
            LOGGER.warn("[USER CONTROLLER] GET /me - Obteniendo perfil del usuario autenticado");
            UserProfileResponse profile = userService.getMyProfile();
            LOGGER.warn("[USER CONTROLLER] ✓ Perfil obtenido: {}", profile.getEmail());
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            LOGGER.error("[USER CONTROLLER] ✗ ERROR al obtener perfil: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@RequestBody UpdateUserProfileRequest request) {
        try {
            LOGGER.warn("[USER CONTROLLER] PUT /me - Actualizando perfil");
            UserProfileResponse updated = userService.updateMyProfile(request);
            LOGGER.warn("[USER CONTROLLER] ✓ Perfil actualizado correctamente");
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            LOGGER.error("[USER CONTROLLER] ✗ ERROR al actualizar perfil: {}", e.getMessage(), e);
            throw e;
        }
    }
}