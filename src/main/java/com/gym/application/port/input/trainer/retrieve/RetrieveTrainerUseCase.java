package com.gym.application.port.input.trainer.retrieve;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.domain.Trainer;

public interface RetrieveTrainerUseCase {
    Trainer getTrainer(AuthCredentials auth, String username);
}