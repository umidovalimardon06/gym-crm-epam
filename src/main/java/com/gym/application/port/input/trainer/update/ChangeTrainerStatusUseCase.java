package com.gym.application.port.input.trainer.update;

import com.gym.domain.Trainer;

public interface ChangeTrainerStatusUseCase {
    void activate(Trainer trainer);
    void deactivate(Trainer trainer);
}
