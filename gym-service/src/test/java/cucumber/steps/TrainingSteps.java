package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.domain.TrainingType;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.TrainerEntity;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.persistence.repository.jpa.TrainerJpaRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TraineeJpaRepository traineeJpaRepository;

    @Autowired
    private TrainerJpaRepository trainerJpaRepository;

    private ResultActions result;

    private Long traineeUserId;
    private Long trainerUserId;

    private String traineeUsername;
    private String traineePassword;

    @Given("a registered trainee with username {string}")
    public void registeredTrainee(String username) {
        traineeUsername = username;
        traineePassword = "password123";

        UserEntity user = new UserEntity(
                "Alimardon",
                "Umidov",
                username,
                passwordEncoder.encode(traineePassword),
                true
        );

        TraineeEntity trainee = new TraineeEntity(null, null, user);

        TraineeEntity saved = traineeJpaRepository.save(trainee);
        traineeUserId = saved.getUser().getId();
    }

    @Given("a registered trainer with username {string}")
    public void registeredTrainer(String username) {
        UserEntity user = new UserEntity(
                "Trainer",
                "Alimardon",
                username,
                passwordEncoder.encode("password123"),
                true
        );

        TrainerEntity trainer = new TrainerEntity(
                TrainingType.CARDIO,
                user
        );

        TrainerEntity saved = trainerJpaRepository.save(trainer);
        trainerUserId = saved.getUser().getId();
    }

    @When("I create a training named {string}")
    public void createTraining(String trainingName) throws Exception {
        String loginResponse = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(
                                        new com.gym.infrastructure.web.dto.auth.LoginRequest(
                                                traineeUsername,
                                                traineePassword
                                        )
                                ))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(loginResponse)
                .get("token")
                .asText();

        String request = """
                {
                    "traineeId": %d,
                    "trainerId": %d,
                    "trainingName": "%s",
                    "trainingType": "CARDIO",
                    "trainingDate": "%s",
                    "trainingDuration": "PT60M"
                }
                """.formatted(
                traineeUserId,
                trainerUserId,
                trainingName,
                LocalDate.now()
        );

        result = mockMvc.perform(
                post("/api/trainings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );
    }

    @Then("the training should be created successfully")
    public void trainingShouldBeCreatedSuccessfully() throws Exception {
        result.andExpect(status().isCreated());
    }
}