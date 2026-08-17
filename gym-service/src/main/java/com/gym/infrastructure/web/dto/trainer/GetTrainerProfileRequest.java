package com.gym.infrastructure.web.dto.trainer;

import jakarta.validation.constraints.NotBlank;

public record GetTrainerProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Trainer username is required")
        String trainerUsername
) {}