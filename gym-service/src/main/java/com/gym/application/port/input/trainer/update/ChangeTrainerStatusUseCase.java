package com.gym.application.port.input.trainer.update;

import com.gym.application.port.input.auth.AuthCredentials;

public interface ChangeTrainerStatusUseCase {
    void activate(AuthCredentials auth);
    void deactivate(AuthCredentials auth);
}
