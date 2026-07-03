package com.gym.application.usecase.trainee;

import com.gym.application.exception.InvalidStateException;
import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainer.update.ChangeTrainerStatusUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeTraineeStatusService implements ChangeTrainerStatusUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChangeTraineeStatusService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;

    public ChangeTraineeStatusService(AuthenticateUseCase authenticator,
                                      TraineeRepository traineeRepository) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional
    public void activate(AuthCredentials auth) {
        setStatus(auth, true);
    }

    @Override
    @Transactional
    public void deactivate(AuthCredentials auth) {
        setStatus(auth, false);
    }

    private void setStatus(AuthCredentials auth, boolean desired) {
        if (auth == null)
            throw new IllegalArgumentException("auth is required");

        log.debug("Change trainee status requested: username={}, desired={}", auth.username(), desired);
        authenticator.verifyCredentials(auth);

        Trainee trainee = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> {
                    log.warn("Change trainee status failed: not found, username={}", auth.username());
                    return new NotFoundException("Trainee not found: " + auth.username());
                });

        if (trainee.isActive() == desired) {
            log.warn("Change trainee status failed: already {}, username={}",
                    desired ? "active" : "inactive", auth.username());
            throw new InvalidStateException(
                    "Trainee is already " + (desired ? "active" : "inactive"));
        }

        trainee.setActive(desired);
        traineeRepository.save(trainee);
        log.info("Trainee status changed to {} for username={}", desired ? "ACTIVE" : "INACTIVE", auth.username());
    }
}