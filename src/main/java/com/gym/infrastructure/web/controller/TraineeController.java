package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeTrainingsUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeUseCase;
import com.gym.application.port.input.trainee.update.ChangeTraineeStatusUseCase;
import com.gym.application.port.input.trainee.update.PopulateTraineeTrainersUserCase;
import com.gym.application.port.input.trainee.update.UpdateTraineeUseCase;
import com.gym.domain.Trainee;
import com.gym.domain.Training;
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.web.dto.trainee.*;
import com.gym.infrastructure.web.dto.training.TrainingResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/trainees", consumes = "application/json", produces = "application/json")
public class TraineeController {
    private static final Logger log = LoggerFactory.getLogger(TraineeController.class);
    private final CreateTraineeUseCase createTraineeUseCase;
    private final DeleteTraineeUseCase deleteTraineeUseCase;
    private final ChangeTraineeStatusUseCase changeTraineeStatusUseCase;
    private final RetrieveTraineeTrainingsUseCase retrieveTraineeTrainingsUseCase;
    private final PopulateTraineeTrainersUserCase populateTraineeTrainersUseCase;
    private final RetrieveTraineeUseCase retrieveTraineeUseCase;
    private final UpdateTraineeUseCase updateTraineeUseCase;
    private final GymMetrics gymMetrics;

    public TraineeController(CreateTraineeUseCase createTraineeUseCase,
                             DeleteTraineeUseCase deleteTraineeUseCase,
                             ChangeTraineeStatusUseCase changeTraineeStatusUseCase,
                             RetrieveTraineeTrainingsUseCase retrieveTraineeTrainingsUseCase,
                             PopulateTraineeTrainersUserCase populateTraineeTrainersUseCase,
                             RetrieveTraineeUseCase retrieveTraineeUseCase,
                             UpdateTraineeUseCase updateTraineeUseCase,
                             GymMetrics gymMetrics) {
        this.createTraineeUseCase = createTraineeUseCase;
        this.deleteTraineeUseCase = deleteTraineeUseCase;
        this.changeTraineeStatusUseCase = changeTraineeStatusUseCase;
        this.retrieveTraineeTrainingsUseCase = retrieveTraineeTrainingsUseCase;
        this.populateTraineeTrainersUseCase = populateTraineeTrainersUseCase;
        this.retrieveTraineeUseCase = retrieveTraineeUseCase;
        this.updateTraineeUseCase = updateTraineeUseCase;
        this.gymMetrics = gymMetrics;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody TraineeRegistrationRequest request) {

        log.info("Received trainee registration request for {} {}",
                request.firstName(), request.lastName());

        Trainee trainee = createTraineeUseCase.create(new CreateTraineeCommand(
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.address()
        ));

        log.info("Trainee registered successfully with username {}", trainee.getUsername());

        RegistrationResponse response = new RegistrationResponse(
                trainee.getUsername(),
                trainee.getPassword()
        );

        gymMetrics.incrementTraineeRegistrations();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProfile(
            @Valid @RequestBody DeleteTraineeProfileRequest request) {

        log.info("Received delete request for trainee {}", request.usernameToDelete());

        deleteTraineeUseCase.deleteTrainee(
                new AuthCredentials(request.username(), request.password()),
                request.usernameToDelete());

        log.info("Deleted trainee {}", request.usernameToDelete());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {

        log.info("Received activation request for trainee {}", request.username());

        changeTraineeStatusUseCase.activate(
                new AuthCredentials(request.username(), request.password()));

        log.info("Activated trainee {}", request.username());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {

        log.info("Received deactivation request for trainee {}", request.username());

        changeTraineeStatusUseCase.deactivate(
                new AuthCredentials(request.username(), request.password()));

        log.info("Deactivated trainee {}", request.username());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainings(
            @PathVariable("username") String username) {

        log.info("Received training retrieval request for trainee {}", username);

        List<Training> traineeTrainings = retrieveTraineeTrainingsUseCase.getTraineeTrainings(
                username,
                null,
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

        log.info("Retrieved {} trainings for trainee {}",
                trainingResponses.size(), username);

        return ResponseEntity.ok(trainingResponses);
    }

    @PutMapping("/trainers")
    public ResponseEntity<PopulateTraineeTrainersResponse> populateTrainers(
            @Valid @RequestBody PopulateTraineeTrainersRequest request) {

        log.info("Received trainer assignment request for trainee {}",
                request.traineeUsername());

        Trainee updated = populateTraineeTrainersUseCase.populateTraineeTrainers(
                new AuthCredentials(request.username(), request.password()),
                request.traineeUsername(),
                request.trainerIds()
        );

        log.info("Updated trainers for trainee {}", updated.getUsername());

        PopulateTraineeTrainersResponse response = new PopulateTraineeTrainersResponse(
                updated.getUsername(),
                updated.getTrainerIds()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> getProfile(
            @Valid @RequestBody GetTraineeProfileRequest request) {

        log.info("Received profile retrieval request for trainee {}",
                request.traineeUsername());

        Trainee t = retrieveTraineeUseCase.getTrainee(
                new AuthCredentials(request.username(), request.password()),
                request.traineeUsername()
        );

        log.info("Retrieved profile for trainee {}", t.getUsername());

        TraineeProfileResponse response = new TraineeProfileResponse(
                t.getFirstName(),
                t.getLastName(),
                t.getDateOfBirth(),
                t.getAddress(),
                t.isActive(),
                t.getTrainerIds()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UpdateTraineeProfileResponse> updateProfile(
            @Valid @RequestBody UpdateTraineeProfileRequest request) {

        log.info("Received profile update request for trainee {}",
                request.username());

        Trainee updated = new Trainee();
        updated.setFirstName(request.firstName());
        updated.setLastName(request.lastName());
        updated.setDateOfBirth(request.dateOfBirth());
        updated.setAddress(request.address());
        updated.setActive(request.isActive());

        Trainee saved = updateTraineeUseCase.updateTraineeProfile(
                new AuthCredentials(request.username(), request.password()),
                updated
        );

        log.info("Updated profile for trainee {}", saved.getUsername());

        UpdateTraineeProfileResponse response = new UpdateTraineeProfileResponse(
                saved.getUsername(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getDateOfBirth(),
                saved.getAddress(),
                saved.isActive(),
                saved.getTrainerIds()
        );

        return ResponseEntity.ok(response);
    }
}