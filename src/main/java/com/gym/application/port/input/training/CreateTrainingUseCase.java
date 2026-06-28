package com.gym.application.port.input.training;

import com.gym.domain.Training;

public interface CreateTrainingUseCase {
    Training addTraining(Training training);
}
