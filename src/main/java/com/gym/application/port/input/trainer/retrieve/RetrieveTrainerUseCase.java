package com.gym.application.port.input.trainer.retrieve;

import com.gym.domain.Trainer;

public interface RetrieveTrainerUseCase {
    Trainer getTrainer(String username);
}
