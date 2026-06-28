package com.gym.application.port.input.trainee;

import com.gym.domain.Trainee;

public interface ChangeTraineePasswordUseCase {
    void changeTraineePassword(Trainee trainee, String newPassword);
}
