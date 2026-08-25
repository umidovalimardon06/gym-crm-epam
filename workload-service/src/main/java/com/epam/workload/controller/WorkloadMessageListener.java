package com.epam.workload.controller;

import com.epam.workload.dto.WorkloadRequest;
import com.epam.workload.service.WorkloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMessageListener {

    private static final Logger log = LoggerFactory.getLogger(WorkloadMessageListener.class);

    private final WorkloadService workloadService;

    public WorkloadMessageListener(WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @JmsListener(destination = "workload.queue")
    public void onWorkloadMessage(WorkloadRequest request) {
        log.info("Received workload message: trainer={}, action={}",
                request != null ? request.username() : null,
                request != null ? request.actionType() : null);

        validate(request);

        workloadService.applyWorkload(request);
        log.info("Workload message processed successfully: trainer={}", request.username());
    }

    private void validate(WorkloadRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Workload message is null");
        if (request.username() == null || request.username().isBlank())
            throw new IllegalArgumentException("username is required");
        if (request.firstName() == null || request.firstName().isBlank())
            throw new IllegalArgumentException("firstName is required");
        if (request.lastName() == null || request.lastName().isBlank())
            throw new IllegalArgumentException("lastName is required");
        if (request.trainingDate() == null)
            throw new IllegalArgumentException("trainingDate is required");
        if (request.trainingDuration() <= 0)
            throw new IllegalArgumentException("trainingDuration must be positive");
        if (request.actionType() == null)
            throw new IllegalArgumentException("actionType is required");
    }
}