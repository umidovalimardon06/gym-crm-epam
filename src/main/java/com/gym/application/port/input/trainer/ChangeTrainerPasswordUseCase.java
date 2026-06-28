package com.gym.application.port.input.trainer;

import com.gym.domain.Trainer;

public interface ChangeTrainerPasswordUseCase {
    void changePassword(Trainer trainer, String newPassword);
}
