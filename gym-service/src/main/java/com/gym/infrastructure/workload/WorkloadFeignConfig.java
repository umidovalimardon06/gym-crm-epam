package com.gym.infrastructure.workload;

import com.gym.infrastructure.secuirty.JwtService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkloadFeignConfig {

    @Bean
    public RequestInterceptor workloadAuthInterceptor(JwtService jwtService) {
        return requestTemplate -> {
            String token = jwtService.generateServiceToken("gym-crm-service");
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}