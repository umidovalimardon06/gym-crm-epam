package com.gym.infrastructure.web.dto.trainer;

import com.gym.domain.TrainingType;
import jakarta.validation.constraints.NotBlank;

public record UpdateTrainerProfileRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        String firstName,
        String lastName,
        TrainingType specialization,
        boolean isActive
) {}