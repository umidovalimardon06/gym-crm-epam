package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrieveTraineeService implements RetrieveTraineeUseCase {

    private static final Logger log = LoggerFactory.getLogger(RetrieveTraineeService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;

    public RetrieveTraineeService(AuthenticateUseCase authenticator,
                                  TraineeRepository traineeRepository) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getTrainee(AuthCredentials auth, String username) {
        validate(username);
        log.debug("Retrieve trainee requested: username={}", username);
        authenticator.authenticate(auth);

        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Retrieve trainee failed: not found, username={}", username);
                    return new NotFoundException("Trainee not found: " + username);
                });
    }

    private void validate(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
    }
}