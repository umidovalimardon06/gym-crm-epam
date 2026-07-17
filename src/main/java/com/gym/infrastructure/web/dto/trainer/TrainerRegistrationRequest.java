package com.gym.infrastructure.web.dto.trainer;

import com.gym.domain.TrainingType;
import jakarta.validation.constraints.NotBlank;

public record TrainerRegistrationRequest(
        @NotBlank(message = "Firstname is required")
        String firstName,
        @NotBlank(message = "Lastname is required")
        String lastName,
        TrainingType specialization
) {
}
