package com.gym.infrastructure.web.dto.trainer;

import jakarta.validation.constraints.NotBlank;

public record GetTrainerProfileRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String trainerUsername
) {}