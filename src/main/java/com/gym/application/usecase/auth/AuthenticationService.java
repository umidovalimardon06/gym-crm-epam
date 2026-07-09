package com.gym.application.usecase.auth;

import com.gym.application.exception.AuthenticationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService implements AuthenticateUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public AuthenticationService(TraineeRepository traineeRepository,
                                 TrainerRepository trainerRepository) {
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public void authenticate(AuthCredentials credentials) {
        User user = requireCredentials(credentials);
        if (!user.isActive()) {
            log.warn("Authentication failed: account inactive, username={}", user.getUsername());
            throw new AuthenticationException("Account is inactive");
        }
        log.debug("Authentication successful for username={}", user.getUsername());
    }

    @Override
    public void verifyCredentials(AuthCredentials credentials) {
        requireCredentials(credentials);
    }

    private User requireCredentials(AuthCredentials c) {
        if (c == null || c.username() == null || c.password() == null) {
            log.warn("Authentication failed: missing credentials");
            throw new AuthenticationException("Invalid credentials");
        }

        User user = traineeRepository.findByUsername(c.username())
                .map(t -> (User) t)
                .or(() -> trainerRepository.findByUsername(c.username()).map(t -> (User) t))
                .orElseThrow(() -> {
                    log.warn("Authentication failed: no user found, username={}", c.username());
                    return new AuthenticationException("Invalid credentials");
                });

        if (!user.getPassword().equals(c.password())) {
            log.warn("Authentication failed: password mismatch, username={}", c.username());
            throw new AuthenticationException("Invalid credentials");
        }

        return user;
    }
}