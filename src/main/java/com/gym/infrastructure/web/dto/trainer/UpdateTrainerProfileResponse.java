package com.gym.infrastructure.web.dto.trainer;

public record UpdateTrainerProfileResponse(
        String username,
        String firstName,
        String lastName,
        String specialization,
        boolean isActive
) {}