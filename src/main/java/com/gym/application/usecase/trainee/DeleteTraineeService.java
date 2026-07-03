package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.exception.TraineeDeletionException;
import com.gym.application.exception.TrainingCreationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTraineeService implements DeleteTraineeUseCase {

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

        authenticator.authenticate(auth);

        if (!auth.username().equals(username)) {
            throw new TraineeDeletionException("Cannot delete another user's account");
        }

        Trainee trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Trainee not found: " + username));

        traineeRepository.deleteByUsername(username);
        return trainee;
    }
}