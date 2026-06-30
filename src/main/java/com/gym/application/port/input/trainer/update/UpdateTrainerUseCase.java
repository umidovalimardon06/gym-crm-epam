package com.gym.application.port.input.trainer.update;

import com.gym.domain.Trainer;

public interface UpdateTrainerUseCase {
    Trainer updateTrainerProfile(String username, Trainer updatedTrainer);
}
