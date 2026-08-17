package com.gym.infrastructure.workload;

import java.time.LocalDate;

public record WorkloadRequest(
        String username,
        String firstName,
        String lastName,
        boolean isActive,
        LocalDate trainingDate,
        int trainingDuration,
        ActionType actionType
) {}