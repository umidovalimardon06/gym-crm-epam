package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.trainee.create.CreateTraineeCommand;
import com.gym.application.port.input.trainee.create.CreateTraineeUseCase;
import com.gym.application.port.input.trainee.delete.DeleteTraineeUseCase;
import com.gym.domain.Trainee;
import com.gym.infrastructure.web.dto.trainee.DeleteTraineeProfileRequest;
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
    private final DeleteTraineeUseCase deleteTraineeUseCase;

    public TraineeController(CreateTraineeUseCase createTraineeUseCase, DeleteTraineeUseCase deleteTraineeUseCase) {
        this.createTraineeUseCase = createTraineeUseCase;
        this.deleteTraineeUseCase = deleteTraineeUseCase;
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
}