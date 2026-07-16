package com.gym.infrastructure.web.dto.training;

import com.gym.domain.TrainingType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Duration;
import java.time.LocalDate;

public record CreateTrainingRequest(
        @NotNull(message = "Trainee ID is required")
        @Positive(message = "Trainee ID must be positive")
        Long traineeId,
        @NotNull(message = "Trainer ID is required")
        @Positive(message = "Trainer ID must be positive")
        Long trainerId,
        @NotBlank(message = "Training name is required")
        String trainingName,
        @NotNull(message = "Training type is required")
        TrainingType trainingType,
        @NotNull(message = "Training date is required")
        @FutureOrPresent(message = "Training date must be today or in the future")
        LocalDate trainingDate,
        @NotNull(message = "Training duration is required")
        @Positive(message = "Training duration must be positive")
        Duration trainingDuration
) {}