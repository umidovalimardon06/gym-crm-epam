package com.gym;

import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.domain.Trainee;
import com.gym.domain.Trainer;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Duration;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        CreateTraineeUseCase createTraineeUseCase = ctx.getBean(CreateTraineeUseCase.class);
        CreateTrainerUseCase createTrainerUseCase = ctx.getBean(CreateTrainerUseCase.class);
        CreateTrainingUseCase createTrainingUseCase = ctx.getBean(CreateTrainingUseCase.class);

        Trainee trainee = createTraineeUseCase.create(new CreateTraineeCommand(
                "Ali","Umidov", LocalDate.of(1999,1,1),"Uzbekistan"
        ));

        Trainer trainer = createTrainerUseCase.createTrainer(new CreateTrainerCommand(
                "Coach","Bob", TrainingType.STRENGTH
        ));

        Training training = new Training(
                null,
                trainee.getUserId(),
                trainer.getUserId(),
                "Morning Strength",
                TrainingType.CARDIO,
                LocalDate.now().plusDays(3),
                Duration.ofMinutes(60)
        );

        Training addedTraining = createTrainingUseCase.addTraining(training);
        System.out.println(addedTraining);

    }
}