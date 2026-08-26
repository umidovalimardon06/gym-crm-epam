package com.epam.workload.dto;

import com.epam.workload.domain.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record WorkloadRequest(
        @NotBlank String username,
        @NotBlank String firstName,
        @NotBlank String lastName,
        boolean isActive,
        @NotNull LocalDate trainingDate,
        @NotNull @Positive Integer trainingDuration,
        @NotNull ActionType actionType
) {
}