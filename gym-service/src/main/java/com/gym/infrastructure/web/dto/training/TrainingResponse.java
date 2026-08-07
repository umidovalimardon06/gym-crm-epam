package com.gym.infrastructure.web.dto.training;

import java.time.Duration;
import java.time.LocalDate;

public record TrainingResponse(
        String trainingName,
        LocalDate trainingDate,
        String trainingType,
        Duration trainingDuration,
        Long trainerId
) {}