package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
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

    public AuthController(
            AuthenticateUseCase authenticateUseCase,
            ChangePasswordUseCase changePasswordUseCase) {
        this.authenticateUseCase = authenticateUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {

        log.info("Authentication request received for username: {}", request.username());

        authenticateUseCase.authenticate(
                new AuthCredentials(
                        request.username(),
                        request.password()
                )
        );

        log.info("Authentication successful for username: {}", request.username());

        return ResponseEntity.ok().build();
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