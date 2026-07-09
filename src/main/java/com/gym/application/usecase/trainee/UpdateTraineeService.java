package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.update.UpdateTraineeUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateTraineeService implements UpdateTraineeUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateTraineeService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;

    public UpdateTraineeService(AuthenticateUseCase authenticator,
                                TraineeRepository traineeRepository) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional
    public Trainee updateTraineeProfile(AuthCredentials auth, Trainee updated) {
        validate(auth, updated);
        log.debug("Update trainee profile requested: username={}", auth.username());
        authenticator.authenticate(auth);

        Trainee existing = traineeRepository.findByUsername(auth.username())
                .orElseThrow(() -> {
                    log.warn("Update trainee failed: not found, username={}", auth.username());
                    return new NotFoundException("Trainee not found: " + auth.username());
                });

        applyChanges(existing, updated);
        Trainee saved = traineeRepository.save(existing);
        log.info("Trainee profile updated: username={}", saved.getUsername());
        return saved;
    }

    private void applyChanges(Trainee target, Trainee source) {
        if (source.getFirstName() != null && !source.getFirstName().isBlank())
            target.setFirstName(source.getFirstName());
        if (source.getLastName() != null && !source.getLastName().isBlank())
            target.setLastName(source.getLastName());
        if (source.getDateOfBirth() != null)
            target.setDateOfBirth(source.getDateOfBirth());
        if (source.getAddress() != null)
            target.setAddress(source.getAddress());
        target.setActive(source.isActive());
    }

    private void validate(AuthCredentials auth, Trainee updated) {
        if (auth == null)
            throw new IllegalArgumentException("auth is required");
        if (updated == null)
            throw new IllegalArgumentException("updated is required");
    }
}