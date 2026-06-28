package com.gym.application.port.input.trainee;

import com.gym.domain.Trainer;

import java.util.List;

public interface RetrieveTraineeTrainersUseCase {
    List<Trainer> getAvailableTrainers(String traineeUsername);
}
