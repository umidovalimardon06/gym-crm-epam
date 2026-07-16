package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

public record GetTraineeProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Trainee username is required")
        String traineeUsername
) {}