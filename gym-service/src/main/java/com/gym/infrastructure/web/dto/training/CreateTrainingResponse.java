package com.gym.infrastructure.web.dto.training;

import com.gym.domain.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public record CreateTrainingResponse(
        String name,
        TrainingType type,
        LocalDate date,
        Duration duration
) {
}