package com.gym.infrastructure.web.dto.trainee;

import java.time.LocalDate;
import java.util.Set;

public record TraineeProfileResponse(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        Set<Long> trainerIds
) {
}