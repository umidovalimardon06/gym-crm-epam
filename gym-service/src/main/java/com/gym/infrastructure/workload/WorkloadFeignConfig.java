package com.gym.infrastructure.workload;

import com.gym.infrastructure.secuirty.JwtService;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkloadFeignConfig {

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