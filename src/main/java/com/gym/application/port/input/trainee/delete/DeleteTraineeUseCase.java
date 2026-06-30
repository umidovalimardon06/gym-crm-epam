package com.gym.application.port.input.trainee.delete;

import com.gym.domain.Trainee;

public interface DeleteTraineeUseCase {
    Trainee deleteTrainee(String username);
}
