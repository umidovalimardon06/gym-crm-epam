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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/trainers", produces = "application/json", consumes = "application/json")
public class TrainerController {

    private static final Logger log = LoggerFactory.getLogger(TrainerController.class);

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

        log.info("Received trainer registration request for {} {}",
                request.firstName(), request.lastName());

        Trainer trainer = createTrainerUseCase.createTrainer(new CreateTrainerCommand(
                request.firstName(),
                request.lastName(),
                request.specialization()
        ));

        log.info("Trainer registered successfully with username {}", trainer.getUsername());

        RegistrationResponse response = new RegistrationResponse(
                trainer.getUsername(),
                trainer.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {

        log.info("Received activation request for trainer {}", request.username());

        changeTrainerStatusUseCase.activate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        log.info("Activated trainer {}", request.username());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {

        log.info("Received deactivation request for trainer {}", request.username());

        changeTrainerStatusUseCase.deactivate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        log.info("Deactivated trainer {}", request.username());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainings(
            @PathVariable("username") String username) {

        log.info("Received training retrieval request for trainer {}", username);

        List<Training> trainerTrainings = retrieveTrainerTrainingsUseCase.getTrainerTrainings(
                username,
                null,
                null,
                null
        );

        List<TrainingResponse> trainingResponses = trainerTrainings.stream()
                .map(t -> new TrainingResponse(
                        t.getTrainingName(),
                        t.getTrainingDate(),
                        t.getTrainingType().name(),
                        t.getTrainingDuration(),
                        t.getTrainerId()
                ))
                .toList();

        log.info("Retrieved {} trainings for trainer {}",
                trainingResponses.size(), username);

        return ResponseEntity.ok(trainingResponses);
    }

    @PostMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getProfile(
            @Valid @RequestBody GetTrainerProfileRequest request) {

        log.info("Received profile retrieval request for trainer {}",
                request.trainerUsername());

        Trainer trainer = retrieveTrainerUseCase.getTrainer(
                new AuthCredentials(request.username(), request.password()),
                request.trainerUsername()
        );

        log.info("Retrieved profile for trainer {}", trainer.getUsername());

        TrainerProfileResponse response = new TrainerProfileResponse(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().name(),
                trainer.isActive()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UpdateTrainerProfileResponse> updateProfile(
            @Valid @RequestBody UpdateTrainerProfileRequest request) {

        log.info("Received profile update request for trainer {}",
                request.username());

        Trainer updated = new Trainer();
        updated.setFirstName(request.firstName());
        updated.setLastName(request.lastName());
        updated.setSpecialization(request.specialization());
        updated.setActive(request.isActive());

        Trainer saved = updateTrainerUseCase.updateTrainerProfile(
                new AuthCredentials(request.username(), request.password()),
                updated
        );

        log.info("Updated profile for trainer {}", saved.getUsername());

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