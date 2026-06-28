package com.gym.application.port.input.trainee;

import com.gym.domain.Trainee;
import com.gym.domain.Trainer;

import java.util.List;

public interface UpdateTraineeUseCase {
    Trainee updateTraineeProfile(String username, Trainee updated);
    List<Trainer> updateAssignedTrainers(String username, List<Trainer> trainers);
}
