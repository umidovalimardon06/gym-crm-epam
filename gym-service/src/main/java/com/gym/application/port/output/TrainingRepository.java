package com.gym.application.port.output;

import com.gym.domain.Training;
import com.gym.domain.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository {
    Training save(Training training);
    List<Training> findTraineeTrainings(String traineeUsername,
                                        LocalDate from,
                                        LocalDate to,
                                        String trainerName,
                                        TrainingType type);

    List<Training> findTrainerTrainings(String trainerUsername,
                                        LocalDate from,
                                        LocalDate to,
                                        String traineeName);
}