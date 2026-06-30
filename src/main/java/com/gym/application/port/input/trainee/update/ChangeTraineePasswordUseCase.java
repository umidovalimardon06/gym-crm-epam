package com.gym.application.port.input.trainee.update;

import com.gym.domain.Trainee;

public interface ChangeTraineePasswordUseCase {
    void changeTraineePassword(Trainee trainee, String newPassword);
}
