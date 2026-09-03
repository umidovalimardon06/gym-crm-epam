package cucumber;

import com.gym.GymCrmApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = GymCrmApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfiguration {
}