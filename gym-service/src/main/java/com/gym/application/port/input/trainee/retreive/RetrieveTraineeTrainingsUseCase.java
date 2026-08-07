package com.gym.application.port.input.trainee.retreive;

import com.gym.domain.Training;
import com.gym.domain.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface RetrieveTraineeTrainingsUseCase {
    List<Training> getTraineeTrainings(String username,
                                       LocalDate from,
                                       LocalDate to,
                                       String trainerName,
                                       TrainingType type);
}