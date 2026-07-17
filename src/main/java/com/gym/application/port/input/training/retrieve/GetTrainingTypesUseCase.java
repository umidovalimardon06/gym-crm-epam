package com.gym.application.port.input.training.retrieve;

import com.gym.domain.TrainingType;

import java.util.List;

public interface GetTrainingTypesUseCase {
    List<TrainingType> getTrainingTypes();
}