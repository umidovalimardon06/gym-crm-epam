package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.trainer.create.CreateTrainerCommand;
import com.gym.application.port.input.trainer.create.CreateTrainerUseCase;
import com.gym.application.port.input.trainer.update.ChangeTrainerStatusUseCase;
import com.gym.domain.Trainer;
import com.gym.infrastructure.web.dto.trainee.ChangeTraineeStatusRequest;
import com.gym.infrastructure.web.dto.trainer.RegistrationResponse;
import com.gym.infrastructure.web.dto.trainer.TrainerRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/trainers", produces = "application/json", consumes = "application/json")
public class TrainerController {
    private final CreateTrainerUseCase createTrainerUseCase;
    private final ChangeTrainerStatusUseCase changeTrainerStatusUseCase;

    public TrainerController(CreateTrainerUseCase createTrainerUseCase, ChangeTrainerStatusUseCase changeTrainerStatusUseCase) {
        this.createTrainerUseCase = createTrainerUseCase;
        this.changeTrainerStatusUseCase = changeTrainerStatusUseCase;
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

    @PatchMapping("/activate")
    public ResponseEntity<Void> activate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTrainerStatusUseCase.activate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivate(
            @Valid @RequestBody ChangeTraineeStatusRequest request) {
        changeTrainerStatusUseCase.deactivate(new AuthCredentials(
                request.username(),
                request.password()
        ));

        return ResponseEntity.ok().build();
    }

}