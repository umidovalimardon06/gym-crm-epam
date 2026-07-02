package com.gym.application.port.input.trainee.delete;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainee;

public interface DeleteTraineeUseCase {
    Trainee deleteTrainee(AuthCredentials auth, String username);
}
