package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateTraineeProfileRequest(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Firstname is required")
        String firstName,
        String lastName,
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,
        boolean isActive
) {}