package cucumber.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import com.gym.infrastructure.secuirty.JwtService;
import com.gym.infrastructure.web.dto.training.CreateTrainingRequest;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDate;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingWorkloadIntegrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainerJpaRepository trainerJpaRepository;

    @Autowired
    private TraineeJpaRepository traineeJpaRepository;

    @Autowired
    private TestContext testContext;

    @Autowired
    private RestClient.Builder restClientBuilder;

    private ResultActions result;

    @Autowired
    private JwtService jwtService;

    @When("I create a {int} minute training for the registered trainer and trainee")
    public void createTraining(int duration) throws Exception {

        Long trainerUserId = trainerJpaRepository
                .findByUser_Username(testContext.getTrainerUsername())
                .orElseThrow()
                .getUser()
                .getId();

        Long traineeUserId = traineeJpaRepository
                .findByUser_Username(testContext.getTraineeUsername())
                .orElseThrow()
                .getUser()
                .getId();

        CreateTrainingRequest request = new CreateTrainingRequest(
                traineeUserId,
                trainerUserId,
                "Integration Training",
                TrainingType.STRENGTH,
                LocalDate.now().plusDays(1),
                Duration.ofMinutes(duration)
        );

        result = mockMvc.perform(
                post("/api/trainings")
                        .with(user("integration.user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    @Then("the training creation response status should be {int}")
    public void trainingCreationResponseStatusShouldBe(int status) throws Exception {
        result.andExpect(status().is(status));
    }


    @Then("the trainer workload should contain {int} minutes")
    public void trainerWorkloadShouldContain(int expectedMinutes) {

        String username = testContext.getTrainerUsername();
        LocalDate trainingDate = LocalDate.now().plusDays(1);

        await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() -> {

                    String token = jwtService.generateServiceToken("gym-service");

                    String body = restClientBuilder
                            .baseUrl("http://localhost:8081")
                            .build()
                            .get()
                            .uri(
                                    "/api/workload/{username}/{year}/{month}",
                                    username,
                                    trainingDate.getYear(),
                                    trainingDate.getMonthValue()
                            )
                            .header("Authorization", "Bearer " + token)
                            .retrieve()
                            .body(String.class);

                    JsonNode response = objectMapper.readTree(body);

                    assertEquals(
                            expectedMinutes,
                            response.get("totalDurationMinutes").asInt()
                    );
                });
    }

    private JsonNode getWorkload(
            String username,
            LocalDate trainingDate,
            int expectedMinutes
    ) {
        try {
            String body = restClientBuilder
                    .baseUrl("http://localhost:8081")
                    .build()
                    .get()
                    .uri(
                            "/api/workload/{username}/{year}/{month}",
                            username,
                            trainingDate.getYear(),
                            trainingDate.getMonthValue()
                    )
                    .retrieve()
                    .body(String.class);

            JsonNode response = objectMapper.readTree(body);

            if (response.has("trainingDuration")
                    && response.get("trainingDuration").asInt() == expectedMinutes) {
                return response;
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
