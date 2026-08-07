package com.gym.application.port.input.trainer.create;

import com.gym.domain.Trainer;

public interface CreateTrainerUseCase {
    Trainer createTrainer(CreateTrainerCommand command);
}
