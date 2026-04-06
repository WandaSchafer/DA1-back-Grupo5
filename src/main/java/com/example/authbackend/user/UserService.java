package com.example.authbackend.user;

import com.example.authbackend.user.dto.UpdateUserProfileRequest;
import com.example.authbackend.user.dto.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getMyProfile() {
        User user = getDefaultUser();

        return mapToProfileResponse(user);
    }

    public UserProfileResponse updateMyProfile(UpdateUserProfileRequest request) {
        User user = getDefaultUser();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            user.setUsername(request.getUsername());
        }

        user.setPhone(request.getPhone());
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setTravelPreferences(request.getTravelPreferences());

        User savedUser = userRepository.save(user);

        return mapToProfileResponse(savedUser);
    }

    private User getDefaultUser() {
        return userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios registrados"));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImageUrl(),
                user.getTravelPreferences()
        );
    }
}