package com.epam.workload.config;

import java.util.Map;
import com.epam.workload.dto.WorkloadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

@EnableJms
@Configuration
public class JmsConfig {
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(
            @Value("${spring.activemq.broker-url}") String brokerUrl,
            @Value("${spring.activemq.user}") String user,
            @Value("${spring.activemq.password}") String password) {

        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(user, password, brokerUrl);

        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        redeliveryPolicy.setInitialRedeliveryDelay(1000);
        redeliveryPolicy.setUseExponentialBackOff(false);
        factory.setRedeliveryPolicy(redeliveryPolicy);

        return factory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setTypeIdMappings(Map.of("workloadRequest", WorkloadRequest.class));
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}