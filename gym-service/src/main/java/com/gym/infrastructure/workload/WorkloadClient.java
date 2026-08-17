package com.gym.infrastructure.workload;

import com.gym.infrastructure.config.WorkloadFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload-service", configuration = WorkloadFeignConfig.class)
public interface WorkloadClient {

    @PostMapping("/api/workload")
    void sendWorkload(@RequestBody WorkloadRequest request);
}