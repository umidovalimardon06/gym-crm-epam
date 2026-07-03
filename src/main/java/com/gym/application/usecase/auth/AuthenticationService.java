package com.gym.application.usecase.auth;

import com.gym.application.exception.AuthenticationException;
import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.port.output.UserRepository;
import com.gym.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService implements AuthenticateUseCase {
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
            throw new AuthenticationException("Account is inactive");
        }
    }

    @Override
    public void verifyCredentials(AuthCredentials credentials) {
        requireCredentials(credentials);
    }

    private User requireCredentials(AuthCredentials c) {
        if (c == null || c.username() == null || c.password() == null)
            throw new AuthenticationException("Invalid credentials");

        User user = traineeRepository.findByUsername(c.username())
                .map(t -> (User) t)
                .or(() -> trainerRepository.findByUsername(c.username()).map(t -> (User) t))
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!user.getPassword().equals(c.password()))
            throw new AuthenticationException("Invalid credentials");

        return user;
    }

}