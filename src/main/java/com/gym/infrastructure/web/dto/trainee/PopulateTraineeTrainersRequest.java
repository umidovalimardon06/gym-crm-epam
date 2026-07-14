package com.gym.infrastructure.web.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PopulateTraineeTrainersRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "traineeUsername is required")
        String traineeUsername,

        @NotEmpty(message = "trainerIds must not be empty")
        Set<@NotNull Long> trainerIds
) {
}