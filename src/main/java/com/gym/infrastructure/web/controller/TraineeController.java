package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.domain.Trainee;
import com.gym.infrastructure.web.dto.trainee.RegistrationResponse;
import com.gym.infrastructure.web.dto.trainee.TraineeRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/trainees",consumes = "application/json",produces = "application/json")
public class TraineeController {
    private final CreateTraineeUseCase createTraineeUseCase;

    public TraineeController(CreateTraineeUseCase createTraineeUseCase) {
        this.createTraineeUseCase = createTraineeUseCase;
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
}