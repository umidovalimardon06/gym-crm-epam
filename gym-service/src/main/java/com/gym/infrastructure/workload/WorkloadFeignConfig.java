package com.gym.infrastructure.workload;

import com.gym.infrastructure.secuirty.JwtService;
import feign.RequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkloadFeignConfig {
    private final Logger log = LoggerFactory.getLogger(WorkloadFeignConfig.class);

    @Bean
    public RequestInterceptor workloadAuthInterceptor(JwtService jwtService) {
        return requestTemplate -> {
            String token = jwtService.generateServiceToken("gym-crm-service");
            requestTemplate.header("Authorization", "Bearer " + token);

            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                requestTemplate.header("X-Transaction-Id", transactionId);
            }
        };
    }
}