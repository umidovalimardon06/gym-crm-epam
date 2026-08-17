package com.gym.application.usecase.training;

import com.gym.application.exception.TrainingCreationException;
import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Trainer;
import com.gym.domain.Training;
import com.gym.infrastructure.workload.ActionType;
import com.gym.infrastructure.workload.WorkloadClient;
import com.gym.infrastructure.workload.WorkloadNotifier;
import com.gym.infrastructure.workload.WorkloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CreateTrainingService implements CreateTrainingUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateTrainingService.class);
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final WorkloadNotifier workloadNotifier;

    public CreateTrainingService(TrainingRepository trainingRepository,
                                 TrainerRepository trainerRepository,
                                 WorkloadNotifier workloadNotifier) {
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.workloadNotifier = workloadNotifier;
    }

    @Override
    @Transactional
    public Training addTraining(Training training) {
        logAddTrainingRequest(training);
        validate(training);

        Training savedTraining = trainingRepository.save(training);
        logTrainingAdded(savedTraining);

        notifyWorkload(training);
        return savedTraining;
    }

    private void notifyWorkload(Training training) {
        Trainer trainer = getTrainerOrElsoReturnNull(training);
        if (trainer == null) return;
        WorkloadRequest request = createWorkloadRequest(training, trainer);
        workloadNotifier.notify(request);
    }

    private Trainer getTrainerOrElsoReturnNull(Training training) {
        Trainer trainer = trainerRepository.findByUserId(training.getTrainerId()).orElse(null);
        if (trainer == null) {
            log.warn("Skipping workload notification: trainer not found, trainerId={}", training.getTrainerId());
            return null;
        }
        return trainer;
    }

    private static WorkloadRequest createWorkloadRequest(Training training, Trainer trainer) {
        return new WorkloadRequest(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                training.getTrainingDate(),
                (int) training.getTrainingDuration().toMinutes(),
                ActionType.ADD
        );
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

    private static void logTrainingAdded(Training savedTraining) {
        log.info("Training added: id={}, traineeId={}, trainerId={}, date={}",
                savedTraining.getId(), savedTraining.getTraineeId(), savedTraining.getTrainerId(), savedTraining.getTrainingDate());
    }

    private static void logAddTrainingRequest(Training training) {
        log.debug("Add training requested: traineeId={}, trainerId={}",
                training != null ? training.getTraineeId() : null,
                training != null ? training.getTrainerId() : null);
    }

}