package com.gym.application.port.input.trainee.update;

import com.gym.domain.Trainee;

public interface ChangeTraineeStatusUseCase {
    void activate(Trainee trainee);
    void deactivate(Trainee trainee);
}
