package com.gym.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.gym.infrastructure,com.gym.application.service")
@PropertySource("classpath:application.properties")
public class AppConfig {
}