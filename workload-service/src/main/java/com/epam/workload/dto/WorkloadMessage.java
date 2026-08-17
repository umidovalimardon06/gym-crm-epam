package com.epam.workload.dto;

import java.time.LocalDate;
import com.epam.workload.domain.ActionType;

public record WorkloadMessage(
        String username,
        String firstName,
        String lastName,
        boolean isActive,
        LocalDate trainingDate,
        int trainingDuration,
        ActionType actionType
) {}