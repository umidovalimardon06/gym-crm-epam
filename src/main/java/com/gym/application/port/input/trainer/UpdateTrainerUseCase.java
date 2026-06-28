package com.gym.application.port.input.trainer;

import com.gym.domain.Trainer;

public interface UpdateTrainerUseCase {
    Trainer updateTrainerProfile(String username, Trainer updatedTrainer);
}
