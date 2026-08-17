package com.gym.application.port.input.trainee.update;

import com.gym.application.port.input.auth.AuthCredentials;

public interface ChangeTraineeStatusUseCase {
    void activate(AuthCredentials auth);
    void deactivate(AuthCredentials auth);
}
