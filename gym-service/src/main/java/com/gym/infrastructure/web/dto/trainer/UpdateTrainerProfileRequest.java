package com.gym.infrastructure.web.dto.trainer;

import com.gym.domain.TrainingType;
import jakarta.validation.constraints.NotBlank;

public record UpdateTrainerProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Firstname is required")
        String firstName,
        @NotBlank(message = "Lastname is required")
        String lastName,
        @NotBlank(message = "Specialization is required")
        TrainingType specialization,
        @NotBlank(message = "isActive is required")
        boolean isActive
) {}