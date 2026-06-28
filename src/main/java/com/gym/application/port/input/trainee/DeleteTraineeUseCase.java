package com.gym.application.port.input.trainee;

import com.gym.domain.Trainee;

public interface DeleteTraineeUseCase {
    Trainee deleteTrainee(String username);
}
