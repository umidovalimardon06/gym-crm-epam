package com.gym.application.port.input.trainer.create;

import com.gym.domain.TrainingType;

public record CreateTrainerCommand(
        String firstName,
        String lastName,
        TrainingType specialization
) {}