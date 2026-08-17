package com.gym.application.usecase.trainee;

import com.gym.application.port.input.trainee.retreive.RetrieveTraineeTrainingsUseCase;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RetrieveTraineeTrainingsService implements RetrieveTraineeTrainingsUseCase {

    private final TrainingRepository trainingRepository;

    public RetrieveTraineeTrainingsService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTraineeTrainings(String username,
                                              LocalDate from,
                                              LocalDate to,
                                              String trainerName,
                                              TrainingType type) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
        return trainingRepository.findTraineeTrainings(username, from, to, trainerName, type);
    }
}