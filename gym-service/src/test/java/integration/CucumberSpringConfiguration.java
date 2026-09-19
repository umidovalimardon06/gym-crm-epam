package integration;

import com.gym.GymCrmApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@CucumberContextConfiguration
@SpringBootTest(classes = GymCrmApplication.class)
@ComponentScan(basePackages = "cucumber.steps")
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}