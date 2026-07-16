package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

public record DeleteTraineeProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Username to delete is required")
        String usernameToDelete
) {
}