package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.domain.Training;
import com.gym.infrastructure.web.dto.training.CreateTrainingRequest;
import com.gym.infrastructure.web.dto.training.CreateTrainingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/trainings", consumes = "application/json",produces = "application/json")
public class TrainingController {
    private final CreateTrainingUseCase createTrainingUseCase;

    public TrainingController(CreateTrainingUseCase createTrainingUseCase) {
        this.createTrainingUseCase = createTrainingUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateTrainingResponse> createTraining(@Valid @RequestBody CreateTrainingRequest request) {
        Training training = createTrainingUseCase.addTraining(new Training(
                request.traineeId(),
                request.trainerId(),
                request.trainingName(),
                request.trainingType(),
                request.trainingDate(),
                request.trainingDuration()
        ));

        CreateTrainingResponse response = new CreateTrainingResponse(
                training.getTrainingName(),
                training.getTrainingType(),
                training.getTrainingDate(),
                training.getTrainingDuration()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}