package com.gym.infrastructure.web.dto.trainer;

public record TrainerProfileResponse(
        String firstName,
        String lastName,
        String specialization,
        boolean isActive
) {}