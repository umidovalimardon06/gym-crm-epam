package com.gym.application.port.input.trainee.retreive;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainee;

public interface RetrieveTraineeUseCase {
    Trainee getTrainee(AuthCredentials auth, String username);
}