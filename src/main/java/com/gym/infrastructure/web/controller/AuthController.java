package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.web.dto.auth.ChangePasswordRequest;
import com.gym.infrastructure.web.dto.auth.LoginRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticateUseCase authenticateUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GymMetrics gymMetrics;

    public AuthController(
            AuthenticateUseCase authenticateUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            GymMetrics gymMetrics) {
        this.authenticateUseCase = authenticateUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.gymMetrics = gymMetrics;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticateUseCase.authenticate(
                    new AuthCredentials(request.username(), request.password())
            );
            return ResponseEntity.ok().<Void>build();
        } catch (Exception e) {
            gymMetrics.incrementAuthFailures();
            throw e;
        }
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        log.info("Password change requested for username: {}", request.username());

        changePasswordUseCase.changePassword(
                request.username(),
                request.oldPassword(),
                request.newPassword()
        );

        log.info("Password changed successfully for username: {}", request.username());

        return ResponseEntity.ok().build();
    }
}