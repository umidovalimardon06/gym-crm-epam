package com.gym.infrastructure.web.controller;

import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.web.dto.auth.ChangePasswordRequest;
import com.gym.infrastructure.web.dto.auth.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints for authentication and credential management")
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

    @Operation(summary = "Authenticate a user",
            description = "Validates the provided username and password. " +
                    "Returns 200 OK if credentials are valid.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
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

    @Operation(summary = "Change user password",
            description = "Changes the password for the given username. " +
                    "Requires the current password for verification.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Old password is incorrect"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
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