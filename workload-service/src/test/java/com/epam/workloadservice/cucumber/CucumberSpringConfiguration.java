package com.epam.workloadservice.cucumber;

import com.epam.workload.WorkloadServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = WorkloadServiceApplication.class)
public class CucumberSpringConfiguration {
}