package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.application.port.input.training.retrieve.GetTrainingTypesUseCase;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.web.dto.training.CreateTrainingRequest;
import com.gym.infrastructure.web.dto.training.CreateTrainingResponse;
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
@RequestMapping(value = "/api/trainings", consumes = "application/json", produces = "application/json")
@Tag(name = "Trainings", description = "Endpoints for creating trainings and retrieving training types")
public class TrainingController {
    private static final Logger log = LoggerFactory.getLogger(TrainingController.class);
    private final CreateTrainingUseCase createTrainingUseCase;
    private final GetTrainingTypesUseCase getTrainingTypesUseCase;
    private final GymMetrics gymMetrics;

    public TrainingController(CreateTrainingUseCase createTrainingUseCase,
                              GetTrainingTypesUseCase getTrainingTypesUseCase,
                              GymMetrics gymMetrics) {
        this.createTrainingUseCase = createTrainingUseCase;
        this.getTrainingTypesUseCase = getTrainingTypesUseCase;
        this.gymMetrics = gymMetrics;
    }

    @Operation(summary = "Create a new training",
            description = "Creates a training session linking a trainee and a trainer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Training created",
                    content = @Content(schema = @Schema(implementation = CreateTrainingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields, or training creation failed"),
            @ApiResponse(responseCode = "404", description = "Trainee or trainer not found")
    })
    @PostMapping
    public ResponseEntity<CreateTrainingResponse> createTraining(
            @Valid @RequestBody CreateTrainingRequest request) {
        log.info("Received training creation request: traineeId={}, trainerId={}, trainingName={}",
                request.traineeId(), request.trainerId(), request.trainingName());

        Training training = createTrainingUseCase.addTraining(new Training(
                request.traineeId(),
                request.trainerId(),
                request.trainingName(),
                request.trainingType(),
                request.trainingDate(),
                request.trainingDuration()
        ));

        log.info("Training '{}' created successfully", training.getTrainingName());

        CreateTrainingResponse response = new CreateTrainingResponse(
                training.getTrainingName(),
                training.getTrainingType(),
                training.getTrainingDate(),
                training.getTrainingDuration()
        );

        gymMetrics.incrementTrainingsCreated();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all training types",
            description = "Retrieves the list of available training types (e.g. Cardio, Strength, Yoga).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Training types retrieved",
                    content = @Content(schema = @Schema(implementation = TrainingType.class)))
    })
    @GetMapping("/types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        log.info("Received request to retrieve training types");
        List<TrainingType> trainingTypes = getTrainingTypesUseCase.getTrainingTypes();
        log.info("Retrieved {} training types", trainingTypes.size());
        return ResponseEntity.ok(trainingTypes);
    }
}