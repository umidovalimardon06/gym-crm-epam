package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import com.gym.infrastructure.web.dto.trainer.TrainerRegistrationRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainerRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainerJpaRepository trainerJpaRepository;

    @Autowired
    private TestContext testContext;

    private ResultActions result;

    @When("I register a trainer with first name {string}, last name {string} and specialization {string}")
    public void registerTrainer(
            String firstName,
            String lastName,
            String specialization) throws Exception {

        TrainerRegistrationRequest request = new TrainerRegistrationRequest(
                firstName,
                lastName,
                TrainingType.valueOf(specialization)
        );

        result = mockMvc.perform(
                post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        String username = objectMapper.readTree(
                result.andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("username").asText();

        testContext.setTrainerUsername(username);
    }

    @When("I update trainer {string} with first name {string}, last name {string} and specialization {string}")
    public void updateTrainer(
            String username,
            String firstName,
            String lastName,
            String specialization) throws Exception {

        String request = """
                {
                  "username": "%s",
                  "password": "password123",
                  "firstName": "%s",
                  "lastName": "%s",
                  "specialization": "%s",
                  "isActive": true
                }
                """.formatted(
                username,
                firstName,
                lastName,
                specialization
        );

        result = mockMvc.perform(
                put("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );
    }

    @Then("the trainer registration response status should be {int}")
    public void trainerRegistrationResponseStatusShouldBe(int status)
            throws Exception {

        result.andExpect(status().is(status));
    }

    @Then("the trainer update response status should be {int}")
    public void trainerUpdateResponseStatusShouldBe(int status)
            throws Exception {

        result.andExpect(status().is(status));
    }
}