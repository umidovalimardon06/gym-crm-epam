package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateTraineeProfileRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive
) {}