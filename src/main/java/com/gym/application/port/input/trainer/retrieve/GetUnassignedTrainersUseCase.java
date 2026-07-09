package com.gym.application.port.input.trainer.retrieve;

import com.gym.domain.Trainer;

import java.util.List;

public interface GetUnassignedTrainersUseCase {
    List<Trainer> getUnassignedTrainers(String traineeUsername);
}