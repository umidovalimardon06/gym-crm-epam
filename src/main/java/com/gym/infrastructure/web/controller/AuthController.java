package com.gym.infrastructure.web.controller;

import com.gym.application.exception.AccountLockedException;
import com.gym.application.exception.AuthenticationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.infrastructure.metrics.GymMetrics;
import com.gym.infrastructure.secuirty.GymUserDetailsService;
import com.gym.infrastructure.secuirty.JwtService;
import com.gym.infrastructure.secuirty.LoginAttemptService;
import com.gym.infrastructure.secuirty.TokenBlacklistService;
import com.gym.infrastructure.web.dto.auth.ChangePasswordRequest;
import com.gym.infrastructure.web.dto.auth.LoginRequest;
import com.gym.infrastructure.web.dto.auth.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints for authentication and credential management")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    private final AuthenticateUseCase authenticateUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GymMetrics gymMetrics;
    private final GymUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(
            AuthenticateUseCase authenticateUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            GymMetrics gymMetrics,
            GymUserDetailsService userDetailsService,
            JwtService jwtService,
            LoginAttemptService loginAttemptService,
            TokenBlacklistService tokenBlacklistService) {
        this.authenticateUseCase = authenticateUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.gymMetrics = gymMetrics;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Operation(summary = "Authenticate a user",
            description = "Validates the provided username and password and returns a JWT " +
                    "on success. Account is locked for 5 minutes after 3 failed attempts.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication successful, token issued"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid required fields"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "423", description = "Account locked due to repeated failed logins")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String username = request.username();

        if (loginAttemptService.isBlocked(username)) {
            log.warn("Login attempt blocked for locked account: {}", username);
            throw new AccountLockedException(
                    "Account locked due to repeated failed login attempts. Try again later.");
        }

        try {
            authenticateUseCase.authenticate(new AuthCredentials(username, request.password()));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for username: {}. Reason: {}", username, e.getMessage());
            loginAttemptService.loginFailed(username);
            gymMetrics.incrementAuthFailures();
            throw e;
        }

        loginAttemptService.loginSucceeded(username);
        log.info("User logged in successfully: {}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "Log out the current user",
            description = "Invalidates the JWT used for this request. Requires authentication.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX_LENGTH);
            tokenBlacklistService.blacklist(token);
            log.info("Token successfully blacklisted for logout");
        }
        return ResponseEntity.ok().build();
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
