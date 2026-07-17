package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.update.PopulateTraineeTrainersUserCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.port.output.TrainerRepository;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PopulateTraineeTrainersService implements PopulateTraineeTrainersUserCase {

    private static final Logger log = LoggerFactory.getLogger(PopulateTraineeTrainersService.class);

    private final AuthenticateUseCase authenticator;
    private final TraineeRepository traineeRepository;

    public PopulateTraineeTrainersService(AuthenticateUseCase authenticator,
                                          TraineeRepository traineeRepository) {
        this.authenticator = authenticator;
        this.traineeRepository = traineeRepository;
    }

    @Override
    @Transactional
    public Trainee populateTraineeTrainers(AuthCredentials auth,
                                           String traineeUsername,
                                           Set<Long> trainerIds) {
        validate(auth, traineeUsername, trainerIds);

        authenticator.authenticate(auth);

        if (!traineeRepository.existsByUsername(traineeUsername)) {
            throw new NotFoundException("Trainee not found: " + traineeUsername);
        }

        return traineeRepository.updateTrainers(traineeUsername, List.copyOf(trainerIds));
    }

    private void validate(AuthCredentials auth, String traineeUsername, Set<Long> trainerIds) {
        if (auth == null)
            throw new IllegalArgumentException("auth is required");
        if (traineeUsername == null || traineeUsername.isBlank())
            throw new IllegalArgumentException("username is required");
        if (trainerIds == null)
            throw new IllegalArgumentException("trainerIds is required (empty set to clear)");
    }
}