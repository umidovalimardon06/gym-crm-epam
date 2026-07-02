package com.gym;

import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeTrainingsUseCase;
import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.application.port.input.trainer.retrieve.RetrieveTrainerTrainingsUseCase;
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

        var createTrainee = ctx.getBean(CreateTraineeUseCase.class);
        var createTrainer = ctx.getBean(CreateTrainerUseCase.class);
        var createTraining = ctx.getBean(CreateTrainingUseCase.class);
        var listTraineeT = ctx.getBean(RetrieveTraineeTrainingsUseCase.class);
        var listTrainerT = ctx.getBean(RetrieveTrainerTrainingsUseCase.class);

        Trainee tr = createTrainee.create(new CreateTraineeCommand(
                "Sam", "Client", LocalDate.of(1988, 3, 3), "X"));
        Trainer c1 = createTrainer.createTrainer(new CreateTrainerCommand(
                "Coach", "One", TrainingType.CARDIO));
        Trainer c2 = createTrainer.createTrainer(new CreateTrainerCommand(
                "Coach", "Two", TrainingType.STRENGTH));

        createTraining.addTraining(new Training(null, tr.getUserId(), c1.getUserId(),
                "Cardio A", TrainingType.CARDIO, LocalDate.now().plusDays(1), Duration.ofMinutes(45)));
        createTraining.addTraining(new Training(null, tr.getUserId(), c2.getUserId(),
                "Strength A", TrainingType.STRENGTH, LocalDate.now().plusDays(2), Duration.ofMinutes(60)));
        createTraining.addTraining(new Training(null, tr.getUserId(), c1.getUserId(),
                "Cardio B", TrainingType.CARDIO, LocalDate.now().plusDays(3), Duration.ofMinutes(30)));

        // No filters
        System.out.println("All: " + listTraineeT.getTraineeTrainings(
                tr.getUsername(), null, null, null, null).size());  // expect 3

        // Filter by type
        System.out.println("Cardio only: " + listTraineeT.getTraineeTrainings(
                tr.getUsername(), null, null, null, TrainingType.CARDIO).size());  // expect 2

        // Filter by trainer name
        System.out.println("Coach One: " + listTraineeT.getTraineeTrainings(
                tr.getUsername(), null, null, "One", null).size());  // expect 2

        // Filter by date range
        System.out.println("First 2 days: " + listTraineeT.getTraineeTrainings(
                tr.getUsername(), LocalDate.now(), LocalDate.now().plusDays(2), null, null).size());  // expect 2

        // Trainer perspective
        System.out.println("Coach One's trainings: " + listTrainerT.getTrainerTrainings(
                c1.getUsername(), null, null, null).size());  // expect 2
    }
}