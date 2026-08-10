package com.gym.infrastructure.workload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
public class WorkloadNotifier {
    private final WorkloadClient workloadClient;
    private static final Logger log = LoggerFactory.getLogger(WorkloadNotifier.class);

    public WorkloadNotifier(WorkloadClient workloadClient) {
        this.workloadClient = workloadClient;
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "fallback")
    public void notify(WorkloadRequest request) {
        workloadClient.sendWorkload(request);
    }

    public void fallback(WorkloadRequest request, Throwable t) {
        log.warn("Workload service unavailable, skipping notification for trainer={}, action={}: {}",
                request.username(), request.actionType(), t.getMessage());
    }
}