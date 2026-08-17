package com.gym.application.port.input.trainer.update;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainer;

public interface UpdateTrainerUseCase {
    Trainer updateTrainerProfile(AuthCredentials auth, Trainer updatedTrainer);
}