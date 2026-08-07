package com.gym.infrastructure.web.dto.trainee;

import java.time.LocalDate;
import java.util.Set;

public record UpdateTraineeProfileResponse(
        String username,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        Set<Long> trainerIds
) {}