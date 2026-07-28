package com.gym.infrastructure.web.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {
    private final Counter hitCounter;

    public MetricsController(MeterRegistry registry) {
        this.hitCounter = Counter.builder("api.hits")
                .description("Total number of hits to the metrics endpoint")
                .tag("endpoint", "/metrics")
                .register(registry);
    }

    @GetMapping("demo/work")
    public String hit() {
        hitCounter.increment();
        return "Hit recorded";
    }
}