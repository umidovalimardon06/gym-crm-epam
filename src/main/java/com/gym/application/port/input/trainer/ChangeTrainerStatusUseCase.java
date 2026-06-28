package com.gym.application.port.input.trainer;

import com.gym.domain.Trainer;

public interface ChangeTrainerStatusUseCase {
    void activate(Trainer trainer);
    void deactivate(Trainer trainer);
}
