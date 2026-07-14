package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;

public record GetTraineeProfileRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String traineeUsername
) {}