package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

public record DeleteTraineeProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        String password,
        String usernameToDelete
) {
}
