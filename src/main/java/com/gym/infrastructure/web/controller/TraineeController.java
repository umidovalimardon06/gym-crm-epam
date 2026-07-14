package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeTrainingsUseCase;
import com.gym.application.port.input.trainee.retreive.RetrieveTraineeUseCase;
import com.gym.application.port.input.trainee.update.ChangeTraineeStatusUseCase;
import com.gym.application.port.input.trainee.update.PopulateTraineeTrainersUserCase;
import com.gym.domain.Trainee;
import com.gym.domain.Training;
import com.gym.infrastructure.web.dto.trainee.*;
import com.gym.infrastructure.web.dto.training.TrainingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/trainees",consumes = "application/json",produces = "application/json")
public class TraineeController {
    private final CreateTraineeUseCase createTraineeUseCase;
    private final DeleteTraineeUseCase deleteTraineeUseCase;
    private final ChangeTraineeStatusUseCase changeTraineeStatusUseCase;
    private final RetrieveTraineeTrainingsUseCase retrieveTraineeTrainingsUseCase;
    private final PopulateTraineeTrainersUserCase populateTraineeTrainersUseCase;
    private final RetrieveTraineeUseCase retrieveTraineeUseCase;

    public TraineeController(CreateTraineeUseCase createTraineeUseCase,
                             DeleteTraineeUseCase deleteTraineeUseCase,
                             ChangeTraineeStatusUseCase changeTraineeStatusUseCase,
                             RetrieveTraineeTrainingsUseCase retrieveTraineeTrainingsUseCase,
                             PopulateTraineeTrainersUserCase populateTraineeTrainersUseCase,
                             RetrieveTraineeUseCase retrieveTraineeUseCase) {
        this.createTraineeUseCase = createTraineeUseCase;
        this.deleteTraineeUseCase = deleteTraineeUseCase;
        this.changeTraineeStatusUseCase = changeTraineeStatusUseCase;
        this.retrieveTraineeTrainingsUseCase = retrieveTraineeTrainingsUseCase;
        this.populateTraineeTrainersUseCase = populateTraineeTrainersUseCase;
        this.retrieveTraineeUseCase = retrieveTraineeUseCase;
    }

    @PostMapping
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody TraineeRegistrationRequest request) {

        Trainee trainee = createTraineeUseCase.create(new CreateTraineeCommand(
                request.firstName(),
                request.lastName(),
                request.dateOfBirth(),
                request.address()
        ));

        RegistrationResponse response = new RegistrationResponse(
                trainee.getUsername(),
                trainee.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProfile(@Valid @RequestBody DeleteTraineeProfileRequest request) {
        deleteTraineeUseCase.deleteTrainee(new AuthCredentials(
                request.username(),
                request.password()), request.usernameToDelete());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTraineeStatusUseCase.activate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTraineeStatusUseCase.deactivate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainingResponse>> getTrainings(@PathVariable("username") String username) {

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

        return ResponseEntity.ok().body(trainingResponses);
    }

    @PutMapping("/trainers")
    public ResponseEntity<PopulateTraineeTrainersResponse> populateTrainers(
            @Valid @RequestBody PopulateTraineeTrainersRequest request) {

        Trainee updated = populateTraineeTrainersUseCase.populateTraineeTrainers(
                new AuthCredentials(request.username(), request.password()),
                request.traineeUsername(),
                request.trainerIds()
        );

        PopulateTraineeTrainersResponse response = new PopulateTraineeTrainersResponse(
                updated.getUsername(),
                updated.getTrainerIds()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> getProfile(
            @Valid @RequestBody GetTraineeProfileRequest request) {

        Trainee t = retrieveTraineeUseCase.getTrainee(
                new AuthCredentials(request.username(), request.password()),
                request.traineeUsername()
        );

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
}