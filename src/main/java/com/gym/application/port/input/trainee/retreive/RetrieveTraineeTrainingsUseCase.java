package com.gym.application.port.input.trainee.retreive;

import com.gym.domain.Training;

import java.util.List;

public interface RetrieveTraineeTrainingsUseCase {
    List<Training> getTraineeTrainings(String username);
}
