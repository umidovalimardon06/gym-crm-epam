package com.gym.application.port.input.trainer.retrieve;

import com.gym.domain.Training;

import java.time.LocalDate;
import java.util.List;

public interface RetrieveTrainerTrainingsUseCase {
    List<Training> getTrainerTrainings(String username,
                                       LocalDate from,
                                       LocalDate to,
                                       String traineeName);
}