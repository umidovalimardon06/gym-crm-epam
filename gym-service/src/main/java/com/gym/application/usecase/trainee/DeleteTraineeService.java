package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.exception.TraineeDeletionException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.port.output.TrainingRepository;
import com.gym.domain.Trainee;
import com.gym.domain.Trainer;
import com.gym.domain.Training;
import com.gym.infrastructure.workload.ActionType;
import com.gym.infrastructure.workload.WorkloadClient;
import com.gym.infrastructure.workload.WorkloadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeleteTraineeService implements DeleteTraineeUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteTraineeService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerRepository trainerRepository;
    private final WorkloadClient workloadClient;

    public DeleteTraineeService(AuthenticateUseCase authenticator,
                                TraineeRepository traineeRepository,
                                TrainingRepository trainingRepository,
                                TrainerRepository trainerRepository,
                                WorkloadClient workloadClient) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
        this.trainingRepository = trainingRepository;
        this.trainerRepository = trainerRepository;
        this.workloadClient = workloadClient;
    }

    @Override
    @Transactional
    public Trainee deleteTrainee(AuthCredentials auth, String username) {
        authCredentialAndUsernameValidation(auth, username);
        logTraineeDeletionRequest(auth, username);

        isUserAttempingToDeleteItsAccount(auth, username);
        Trainee trainee = getTraineeFromDatabaseOrThrowExcerption(username);
        List<Training> trainings = trainingRepository.findTraineeTrainings(username, null, null, null, null);

        traineeRepository.deleteByUsername(username);
        log.info("Trainee deleted: username={}", username);

        notifyWorkloadServiceAboutDeletedTrainings(trainings);
        return trainee;
    }

    private void notifyWorkloadServiceAboutDeletedTrainings(List<Training> trainings) {
        for (Training training : trainings) {
            notifyWorkload(training);
        }
    }

    private void notifyWorkload(Training training) {
        Trainer trainer = trainerRepository.findById(training.getTrainerId()).orElse(null);
        if (trainer == null) {
            log.warn("Skipping workload notification: trainer not found, trainerId={}", training.getTrainerId());
            return;
        }

        WorkloadRequest request = new WorkloadRequest(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                training.getTrainingDate(),
                (int) training.getTrainingDuration().toMinutes(),
                ActionType.DELETE
        );

        workloadClient.sendWorkload(request);
    }

    private Trainee getTraineeFromDatabaseOrThrowExcerption(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Delete trainee failed: not found, username={}", username);
                    return new NotFoundException("Trainee not found: " + username);
                });
    }

    private static void isUserAttempingToDeleteItsAccount(AuthCredentials auth, String username) {
        if (!auth.username().equals(username)) {
            log.warn("Delete trainee denied: {} attempted to delete {}", auth.username(), username);
            throw new TraineeDeletionException("Cannot delete another user's account");
        }
    }

    private void logTraineeDeletionRequest(AuthCredentials auth, String username) {
        log.debug("Delete trainee requested: username={}", username);
        authenticator.authenticate(auth);
    }

    private static void authCredentialAndUsernameValidation(AuthCredentials auth, String username) {
        if (auth == null)
            throw new TraineeDeletionException("auth is required");
        if (username == null || username.isBlank())
            throw new TraineeDeletionException("username is required");
    }
}