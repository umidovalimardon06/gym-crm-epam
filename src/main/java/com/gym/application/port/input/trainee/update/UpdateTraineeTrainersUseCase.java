package com.gym.application.port.input.trainee.update;

import com.gym.domain.Trainer;

import java.util.List;

public interface UpdateTraineeTrainersUseCase {
    List<Trainer> updateAssignedTrainers(String traineeUsername, List<Long> trainerIds);
}