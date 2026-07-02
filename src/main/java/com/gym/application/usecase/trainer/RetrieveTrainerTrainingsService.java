package com.gym.application.usecase.trainer;

import com.gym.application.port.input.trainer.retrieve.RetrieveTrainerTrainingsUseCase;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Training;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RetrieveTrainerTrainingsService implements RetrieveTrainerTrainingsUseCase {

    private final TrainingRepository trainingRepository;

    public RetrieveTrainerTrainingsService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainerTrainings(String username,
                                              LocalDate from,
                                              LocalDate to,
                                              String traineeName) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
        return trainingRepository.findTrainerTrainings(username, from, to, traineeName);
    }
}