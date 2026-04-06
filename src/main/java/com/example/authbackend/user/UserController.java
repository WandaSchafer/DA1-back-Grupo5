package com.example.authbackend.user;

import com.example.authbackend.user.dto.UpdateUserProfileRequest;
import com.example.authbackend.user.dto.UserProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(@RequestBody UpdateUserProfileRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(request));
    }
}