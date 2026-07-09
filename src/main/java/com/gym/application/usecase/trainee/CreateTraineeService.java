package com.gym.application.usecase.trainee;

import com.gym.application.exception.CreationCommandException;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.output.TraineeRepository;
import com.gym.application.service.PasswordGenerator;
import com.gym.application.service.UsernameGenerator;
import com.gym.domain.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTraineeService implements CreateTraineeUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTraineeService.class);

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
        log.debug("Create trainee requested: firstName={}, lastName={}",
                command != null ? command.firstName() : null,
                command != null ? command.lastName() : null);
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

        Trainee saved = traineeRepository.save(trainee);
        log.info("Trainee created: username={}", saved.getUsername());
        return saved;
    }

    private void validate(CreateTraineeCommand cmd) {
        if (cmd == null) throw new CreationCommandException("command is required");
        if (cmd.firstName() == null || cmd.firstName().isBlank())
            throw new CreationCommandException("firstName is required");
        if (cmd.lastName() == null || cmd.lastName().isBlank())
            throw new CreationCommandException("lastName is required");
    }
}