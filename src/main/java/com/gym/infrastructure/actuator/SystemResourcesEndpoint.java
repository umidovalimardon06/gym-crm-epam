package com.gym.infrastructure.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Endpoint(id = "system-resources")
public class SystemResourcesEndpoint {

    @ReadOperation
    public Map<String, Object> resources() {
        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long used = total - free;

        return Map.of(
                "availableProcessors", runtime.availableProcessors(),
                "totalMemoryMB", total / 1024 / 1024,
                "usedMemoryMB", used / 1024 / 1024,
                "freeMemoryMB", free / 1024 / 1024,
                "maxMemoryMB", runtime.maxMemory() / 1024 / 1024
        );
    }
}