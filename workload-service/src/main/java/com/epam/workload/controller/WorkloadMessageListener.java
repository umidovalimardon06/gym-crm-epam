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
        log.info("Received workload message: trainer={}, action={}", request.username(), request.actionType());
        workloadService.applyWorkload(request);
        log.info("Workload message processed successfully: trainer={}", request.username());
    }
}