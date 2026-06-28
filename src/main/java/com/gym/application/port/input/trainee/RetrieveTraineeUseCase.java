package com.gym.application.port.input.trainee;

import com.gym.domain.Trainee;

public interface RetrieveTraineeUseCase {
    Trainee getTrainee(String username);
}
