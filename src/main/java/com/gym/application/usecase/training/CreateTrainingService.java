package com.gym.application.usecase.training;

import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Training;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CreateTrainingService implements CreateTrainingUseCase {

    private final TrainingRepository trainingRepository;

    public CreateTrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Training addTraining(Training training) {
        validate(training);
        return trainingRepository.save(training);
    }

    private void validate(Training t) {
        if (t == null)
            throw new IllegalArgumentException("training is required");
        if (t.getTraineeId() == null)
            throw new IllegalArgumentException("traineeId is required");
        if (t.getTrainerId() == null)
            throw new IllegalArgumentException("trainerId is required");
        if (t.getTrainingName() == null || t.getTrainingName().isBlank())
            throw new IllegalArgumentException("trainingName is required");
        if (t.getTrainingType() == null)
            throw new IllegalArgumentException("trainingType is required");
        if (t.getTrainingDate() == null)
            throw new IllegalArgumentException("trainingDate is required");
        if (t.getTrainingDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("trainingDate cannot be in the past");
        if (t.getTrainingDuration() == null
                || t.getTrainingDuration().isZero()
                || t.getTrainingDuration().isNegative())
            throw new IllegalArgumentException("trainingDuration must be positive");
    }
}