package com.gym.application.port.input.trainer.update;

import com.gym.domain.Trainer;

public interface ChangeTrainerPasswordUseCase {
    void changePassword(Trainer trainer, String newPassword);
}
