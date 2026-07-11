package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.domain.Trainer;
import com.gym.infrastructure.web.dto.trainer.RegistrationResponse;
import com.gym.infrastructure.web.dto.trainer.TrainerRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/trainers", produces = "application/json", consumes = "application/json")
public class TrainerController {
    private final CreateTrainerUseCase createTrainerUseCase;

    public TrainerController(CreateTrainerUseCase createTrainerUseCase) {
        this.createTrainerUseCase = createTrainerUseCase;
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
}