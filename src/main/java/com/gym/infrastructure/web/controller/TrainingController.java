package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.training.create.CreateTrainingUseCase;
import com.gym.application.port.input.training.retrieve.GetTrainingTypesUseCase;
import com.gym.domain.Training;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.web.dto.training.CreateTrainingRequest;
import com.gym.infrastructure.web.dto.training.CreateTrainingResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/trainings", consumes = "application/json",produces = "application/json")
public class TrainingController {
    private final CreateTrainingUseCase createTrainingUseCase;
    private final GetTrainingTypesUseCase getTrainingTypesUseCase;

    public TrainingController(CreateTrainingUseCase createTrainingUseCase, GetTrainingTypesUseCase getTrainingTypesUseCase) {
        this.createTrainingUseCase = createTrainingUseCase;
        this.getTrainingTypesUseCase = getTrainingTypesUseCase;
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

    @GetMapping("/types")
    public ResponseEntity<List<TrainingType>> getTrainingTypes() {
        List<TrainingType> trainingTypes = getTrainingTypesUseCase.getTrainingTypes();
        return ResponseEntity.ok(trainingTypes);
    }
}