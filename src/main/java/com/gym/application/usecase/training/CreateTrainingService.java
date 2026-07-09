package com.gym.application.usecase.training;

import com.gym.application.exception.TrainingCreationException;
import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CreateTrainingService implements CreateTrainingUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTrainingService.class);

    private final TrainingRepository trainingRepository;

    public CreateTrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Training addTraining(Training training) {
        log.debug("Add training requested: traineeId={}, trainerId={}",
                training != null ? training.getTraineeId() : null,
                training != null ? training.getTrainerId() : null);
        validate(training);

        Training saved = trainingRepository.save(training);
        log.info("Training added: id={}, traineeId={}, trainerId={}, date={}",
                saved.getId(), saved.getTraineeId(), saved.getTrainerId(), saved.getTrainingDate());
        return saved;
    }

    private void validate(Training t) {
        if (t == null)
            throw new TrainingCreationException("training is required");
        if (t.getTraineeId() == null)
            throw new TrainingCreationException("traineeId is required");
        if (t.getTrainerId() == null)
            throw new TrainingCreationException("trainerId is required");
        if (t.getTrainingName() == null || t.getTrainingName().isBlank())
            throw new TrainingCreationException("trainingName is required");
        if (t.getTrainingType() == null)
            throw new TrainingCreationException("trainingType is required");
        if (t.getTrainingDate() == null)
            throw new TrainingCreationException("trainingDate is required");
        if (t.getTrainingDate().isBefore(LocalDate.now()))
            throw new TrainingCreationException("trainingDate cannot be in the past");
        if (t.getTrainingDuration() == null
                || t.getTrainingDuration().isZero()
                || t.getTrainingDuration().isNegative())
            throw new TrainingCreationException("trainingDuration must be positive");
    }
}