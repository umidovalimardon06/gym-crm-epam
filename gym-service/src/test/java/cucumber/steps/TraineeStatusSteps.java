package cucumber.steps;

import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeStatusSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TraineeJpaRepository traineeJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ResultActions result;

    @Given("an inactive trainee with username {string} and password {string}")
    public void inactiveTrainee(String username, String password) {
        UserEntity user = new UserEntity(
                "Alimardon",
                "Umidov",
                username,
                passwordEncoder.encode(password),
                false
        );

        traineeJpaRepository.save(
                new TraineeEntity(null, null, user)
        );
    }

    @When("I deactivate the trainee {string} with password {string}")
    public void deactivateTrainee(String username, String password) throws Exception {
        String request = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        result = mockMvc.perform(
                patch("/api/trainees/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );
    }

    @When("I activate the trainee {string} with password {string}")
    public void activateTrainee(String username, String password) throws Exception {
        String request = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        result = mockMvc.perform(
                patch("/api/trainees/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );
    }

    @Then("the trainee status response should be {int}")
    public void traineeStatusResponseShouldBe(int status) throws Exception {
        result.andExpect(status().is(status));
    }
}