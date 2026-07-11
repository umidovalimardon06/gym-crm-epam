package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.infrastructure.web.dto.auth.ChangePasswordRequest;
import com.gym.infrastructure.web.dto.auth.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthenticateUseCase authenticateUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    public AuthController(AuthenticateUseCase authenticateUseCase, ChangePasswordUseCase changePasswordUseCase) {
        this.authenticateUseCase = authenticateUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest request) {
        authenticateUseCase.authenticate(new AuthCredentials(
                request.firstName(),
                request.lastName()
        ));

        return ResponseEntity.ok().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.changePassword(
                request.username(),
                request.oldPassword(),
                request.newPassword()
        );
        return ResponseEntity.ok().build();
    }

}