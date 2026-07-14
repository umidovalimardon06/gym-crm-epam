package com.gym.infrastructure.web.dto.trainee;

import java.util.Set;

public record PopulateTraineeTrainersResponse(
        String username,
        Set<Long> trainerIds
) {
}