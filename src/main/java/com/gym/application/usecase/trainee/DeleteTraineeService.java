package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.exception.TraineeDeletionException;
import com.gym.application.exception.TrainingCreationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTraineeService implements DeleteTraineeUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteTraineeService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;

    public DeleteTraineeService(AuthenticateUseCase authenticator,
                                TraineeRepository traineeRepository) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional
    public Trainee deleteTrainee(AuthCredentials auth, String username) {
        if (auth == null)
            throw new TraineeDeletionException("auth is required");
        if (username == null || username.isBlank())
            throw new TraineeDeletionException("username is required");

        log.debug("Delete trainee requested: username={}", username);
        authenticator.authenticate(auth);

        if (!auth.username().equals(username)) {
            log.warn("Delete trainee denied: {} attempted to delete {}", auth.username(), username);
            throw new TraineeDeletionException("Cannot delete another user's account");
        }

        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Delete trainee failed: not found, username={}", username);
                    return new NotFoundException("Trainee not found: " + username);
                });

        traineeRepository.deleteByUsername(username);
        log.info("Trainee deleted: username={}", username);
        return trainee;
    }
}