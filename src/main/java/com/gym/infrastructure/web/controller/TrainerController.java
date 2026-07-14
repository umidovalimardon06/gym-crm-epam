package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.application.port.input.trainer.retrieve.RetrieveTrainerTrainingsUseCase;
import com.gym.application.port.input.trainer.retrieve.RetrieveTrainerUseCase;
import com.gym.application.port.input.trainer.update.ChangeTrainerStatusUseCase;
import com.gym.application.port.input.trainer.update.UpdateTrainerUseCase;
import com.gym.domain.Trainer;
import com.gym.domain.Training;
import com.gym.infrastructure.web.dto.trainee.ChangeTraineeStatusRequest;
import com.gym.infrastructure.web.dto.trainer.*;
import com.gym.infrastructure.web.dto.training.TrainingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/trainers", produces = "application/json", consumes = "application/json")
public class TrainerController {
    private final CreateTrainerUseCase createTrainerUseCase;
    private final ChangeTrainerStatusUseCase changeTrainerStatusUseCase;
    private final RetrieveTrainerTrainingsUseCase retrieveTrainerTrainingsUseCase;
    private final RetrieveTrainerUseCase retrieveTrainerUseCase;
    private final UpdateTrainerUseCase updateTrainerUseCase;

    public TrainerController(CreateTrainerUseCase createTrainerUseCase,
                             ChangeTrainerStatusUseCase changeTrainerStatusUseCase,
                             RetrieveTrainerTrainingsUseCase retrieveTrainerTrainingsUseCase,
                             RetrieveTrainerUseCase retrieveTrainerUseCase,
                             UpdateTrainerUseCase updateTrainerUseCase) {
        this.createTrainerUseCase = createTrainerUseCase;
        this.changeTrainerStatusUseCase = changeTrainerStatusUseCase;
        this.retrieveTrainerTrainingsUseCase = retrieveTrainerTrainingsUseCase;
        this.retrieveTrainerUseCase = retrieveTrainerUseCase;
        this.updateTrainerUseCase = updateTrainerUseCase;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody TrainerRegistrationRequest request) {

        Trainer trainer = createTrainerUseCase.createTrainer(new CreateTrainerCommand(
                request.firstName(),
                request.lastName(),
                request.specialization()
        ));

        RegistrationResponse response = new RegistrationResponse(
                trainer.getUsername(),
                trainer.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTrainerStatusUseCase.activate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTrainerStatusUseCase.deactivate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainings(@PathVariable("username") String username) {

        List<Training> traineeTrainings = retrieveTrainerTrainingsUseCase.getTrainerTrainings(
                username,
                null,
                null,
                null
        );

        List<TrainingResponse> trainingResponses = traineeTrainings.stream()
                .map(t -> new TrainingResponse(
                        t.getTrainingName(),
                        t.getTrainingDate(),
                        t.getTrainingType().name(),
                        t.getTrainingDuration(),
                        t.getTrainerId()
                ))
                .toList();

        return ResponseEntity.ok().body(trainingResponses);
    }

    @PostMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getProfile(
            @Valid @RequestBody GetTrainerProfileRequest request) {

        Trainer t = retrieveTrainerUseCase.getTrainer(
                new AuthCredentials(request.username(), request.password()),
                request.trainerUsername()
        );

        TrainerProfileResponse response = new TrainerProfileResponse(
                t.getFirstName(),
                t.getLastName(),
                t.getSpecialization().name(),
                t.isActive()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UpdateTrainerProfileResponse> updateProfile(
            @Valid @RequestBody UpdateTrainerProfileRequest request) {

        Trainer updated = new Trainer();
        updated.setFirstName(request.firstName());
        updated.setLastName(request.lastName());
        updated.setSpecialization(request.specialization());
        updated.setActive(request.isActive());

        Trainer saved = updateTrainerUseCase.updateTrainerProfile(
                new AuthCredentials(request.username(), request.password()),
                updated
        );

        UpdateTrainerProfileResponse response = new UpdateTrainerProfileResponse(
                saved.getUsername(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getSpecialization().name(),
                saved.isActive()
        );

        return ResponseEntity.ok(response);
    }
}