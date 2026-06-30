package com.gym.application.usecase.trainer;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainer.retrieve.RetrieveTrainerUseCase;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetrieveTrainerService implements RetrieveTrainerUseCase {

    private final AuthenticateUseCase authenticator;
    private final TrainerRepository trainerRepository;

    public RetrieveTrainerService(AuthenticateUseCase authenticator,
                                  TrainerRepository trainerRepository) {
        this.authenticator = authenticator;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainer(AuthCredentials auth, String username) {
        validate(username);
        authenticator.authenticate(auth);

        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainer not found: " + username));
    }

    private void validate(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
    }
}