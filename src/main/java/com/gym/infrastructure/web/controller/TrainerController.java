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
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.web.dto.trainee.ChangeTraineeStatusRequest;
import com.gym.infrastructure.web.dto.trainer.*;
import com.gym.infrastructure.web.dto.training.TrainingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/trainers", produces = "application/json", consumes = "application/json")
@Tag(name = "Trainers", description = "Endpoints for managing trainer profiles, status, and training history")
public class TrainerController {
    private static final Logger log = LoggerFactory.getLogger(TrainerController.class);
    private final CreateTrainerUseCase createTrainerUseCase;
    private final ChangeTrainerStatusUseCase changeTrainerStatusUseCase;
    private final RetrieveTrainerTrainingsUseCase retrieveTrainerTrainingsUseCase;
    private final RetrieveTrainerUseCase retrieveTrainerUseCase;
    private final UpdateTrainerUseCase updateTrainerUseCase;
    private final GymMetrics gymMetrics;

    public TrainerController(CreateTrainerUseCase createTrainerUseCase,
                             ChangeTrainerStatusUseCase changeTrainerStatusUseCase,
                             RetrieveTrainerTrainingsUseCase retrieveTrainerTrainingsUseCase,
                             RetrieveTrainerUseCase retrieveTrainerUseCase,
                             UpdateTrainerUseCase updateTrainerUseCase,
                             GymMetrics gymMetrics) {
        this.createTrainerUseCase = createTrainerUseCase;
        this.changeTrainerStatusUseCase = changeTrainerStatusUseCase;
        this.retrieveTrainerTrainingsUseCase = retrieveTrainerTrainingsUseCase;
        this.retrieveTrainerUseCase = retrieveTrainerUseCase;
        this.updateTrainerUseCase = updateTrainerUseCase;
        this.gymMetrics = gymMetrics;
    }

    @Operation(summary = "Register a new trainer",
            description = "Creates a trainer profile. Username and password are system-generated. " +
                    "No authentication required")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Trainer created",
                    content = @Content(schema = @Schema(implementation = RegistrationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields")
    })
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

        gymMetrics.incrementTrainerRegistrations();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Activate a trainer",
            description = "Sets the trainer's status to active. Requires authentication credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer activated"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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

    @Operation(summary = "Deactivate a trainer",
            description = "Sets the trainer's status to inactive. Requires authentication credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainer deactivated"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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

    @Operation(summary = "Get a trainer's trainings",
            description = "Retrieves the list of trainings associated with the given trainer username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainings retrieved",
                    content = @Content(schema = @Schema(implementation = TrainingResponse.class))),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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

    @Operation(summary = "Get a trainer's profile",
            description = "Retrieves the profile details of the given trainer. Requires authentication credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved",
                    content = @Content(schema = @Schema(implementation = TrainerProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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

    @Operation(summary = "Update a trainer's profile",
            description = "Updates the profile fields of the given trainer. Requires authentication credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated",
                    content = @Content(schema = @Schema(implementation = UpdateTrainerProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
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