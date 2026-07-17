package com.gym.infrastructure.web.dto.trainer;

import jakarta.validation.constraints.NotBlank;

public record ChangeTrainerStatusRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password
) {
}
