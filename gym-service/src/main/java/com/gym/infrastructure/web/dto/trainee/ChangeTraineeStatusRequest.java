package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

public record ChangeTraineeStatusRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
) {
}
