package com.gym.application.port.input.trainee.create;

import com.gym.domain.Trainee;

public interface CreateTraineeUseCase {
    Trainee create(CreateTraineeCommand command);
}
