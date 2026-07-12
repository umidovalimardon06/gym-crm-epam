package com.gym.infrastructure.web.dto.training;

import com.gym.domain.TrainingType;

import java.time.Duration;
import java.time.LocalDate;

public record CreateTrainingRequest(
        Long traineeId,
        Long trainerId,
        String trainingName,
        TrainingType trainingType,
        LocalDate trainingDate,
        Duration trainingDuration
) {
}