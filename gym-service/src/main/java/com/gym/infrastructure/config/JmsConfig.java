package com.gym.infrastructure.config;

import java.util.Map;
import org.springframework.context.annotation.Bean;
import com.gym.infrastructure.workload.WorkloadRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

@Configuration
public class JmsConfig {
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setTypeIdMappings(Map.of("workloadRequest", WorkloadRequest.class));
        return converter;
    }
}