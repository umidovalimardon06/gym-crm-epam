package com.gym.application.port.input.trainee.update;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainee;

public interface UpdateTraineeUseCase {
    Trainee updateTraineeProfile(AuthCredentials auth, Trainee updated);
}