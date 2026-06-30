package com.gym.application.usecase.trainee;

import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.service.PasswordGenerator;
import com.gym.application.service.UsernameGenerator;
import com.gym.domain.Trainee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTraineeService implements CreateTraineeUseCase {

    private final TraineeRepository traineeRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    public CreateTraineeService(TraineeRepository traineeRepository,
                                UsernameGenerator usernameGenerator,
                                PasswordGenerator passwordGenerator) {
        this.traineeRepository = traineeRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    @Transactional
    public Trainee create(CreateTraineeCommand command) {
        validate(command);

        String username = usernameGenerator.generate(command.firstName(), command.lastName());
        String password = passwordGenerator.generatePassword();

        Trainee trainee = new Trainee(
                null,
                command.firstName(),
                command.lastName(),
                username,
                password,
                command.dateOfBirth(),
                command.address()
        );
        trainee.setActive(true);
        return traineeRepository.save(trainee);
    }

    private void validate(CreateTraineeCommand cmd) {
        if (cmd == null) throw new IllegalArgumentException("command is required");
        if (cmd.firstName() == null || cmd.firstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (cmd.lastName() == null || cmd.lastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
    }
}