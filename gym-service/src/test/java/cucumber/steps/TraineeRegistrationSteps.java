package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.infrastructure.web.dto.trainee.TraineeRegistrationRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TraineeRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    private ResultActions result;

    @When("I register a trainee with first name {string}, last name {string}")
    public void registerTrainee(
            String firstName,
            String lastName) throws Exception {

        TraineeRegistrationRequest request = new TraineeRegistrationRequest(
                firstName,
                lastName,
                LocalDate.of(2000, 1, 1),
                "Tashkent"
        );

        result = mockMvc.perform(
                post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        String username = objectMapper.readTree(
                result.andReturn()
                        .getResponse()
                        .getContentAsString()
        ).get("username").asText();

        testContext.setTraineeUsername(username);
    }

    @Then("the trainee registration response status should be {int}")
    public void traineeRegistrationResponseStatusShouldBe(int status)
            throws Exception {

        result.andExpect(status().is(status));
    }
}