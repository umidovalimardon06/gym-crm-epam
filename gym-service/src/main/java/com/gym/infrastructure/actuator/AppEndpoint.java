package com.gym.infrastructure.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
@Endpoint(id = "app-info")
public class AppEndpoint {
    private final Instant startTime = Instant.now();

    @ReadOperation
    public Map<String,Object> getAppInfo() {
        return Map.of(
                "name", "Gym CRM",
                "version", "1.0.0",
                "startedAt", startTime.toString(),
                "uptimeSeconds", java.time.Duration.between(startTime, Instant.now()).toSeconds(),
                "javaVersion", System.getProperty("java.version")
        );
    }
}