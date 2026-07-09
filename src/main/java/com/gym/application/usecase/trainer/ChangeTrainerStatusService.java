package com.gym.application.usecase.trainer;

import com.gym.application.exception.InvalidStateException;
import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainer.update.ChangeTrainerStatusUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeTrainerStatusService implements ChangeTrainerStatusUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChangeTrainerStatusService.class);

    private final AuthenticateUseCase authenticator;
    private final TrainerRepository trainerRepository;

    public ChangeTrainerStatusService(AuthenticateUseCase authenticator,
                                      TrainerRepository trainerRepository) {
        this.authenticator = authenticator;
        this.trainerRepository = trainerRepository;
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

        log.debug("Change trainer status requested: username={}, desired={}", auth.username(), desired);
        authenticator.verifyCredentials(auth);

        Trainer trainer = trainerRepository.findByUsername(auth.username())
                .orElseThrow(() -> {
                    log.warn("Change trainer status failed: not found, username={}", auth.username());
                    return new NotFoundException("Trainer not found: " + auth.username());
                });

        if (trainer.isActive() == desired) {
            log.warn("Change trainer status failed: already {}, username={}",
                    desired ? "active" : "inactive", auth.username());
            throw new InvalidStateException(
                    "Trainer is already " + (desired ? "active" : "inactive"));
        }

        trainer.setActive(desired);
        trainerRepository.save(trainer);
        log.info("Trainer status changed to {} for username={}", desired ? "ACTIVE" : "INACTIVE", auth.username());
    }
}