package com.gym.application.usecase.trainer;

import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.output.TrainerRepository;
import com.gym.application.service.PasswordGenerator;
import com.gym.application.service.UsernameGenerator;
import com.gym.domain.Trainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTrainerService implements CreateTrainerUseCase {

    private final TrainerRepository trainerRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;

    public CreateTrainerService(TrainerRepository trainerRepository,
                                UsernameGenerator usernameGenerator,
                                PasswordGenerator passwordGenerator) {
        this.trainerRepository = trainerRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    @Transactional
    public Trainer createTrainer(CreateTrainerCommand command) {
        validate(command);

        String username = usernameGenerator.generate(command.firstName(), command.lastName());
        String password = passwordGenerator.generatePassword();

        Trainer trainer = new Trainer(
                null,
                command.firstName(),
                command.lastName(),
                username,
                password,
                command.specialization()
        );
        trainer.setActive(true);

        return trainerRepository.save(trainer);
    }

    private void validate(CreateTrainerCommand cmd) {
        if (cmd == null)
            throw new IllegalArgumentException("command is required");
        if (cmd.firstName() == null || cmd.firstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (cmd.lastName() == null || cmd.lastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
        if (cmd.specialization() == null)
            throw new IllegalArgumentException("specialization is required");
    }
}