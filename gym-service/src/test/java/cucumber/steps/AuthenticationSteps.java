package cucumber.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gym.infrastructure.persistence.entity.TraineeEntity;
import com.gym.infrastructure.persistence.entity.UserEntity;
import com.gym.infrastructure.persistence.repository.jpa.TraineeJpaRepository;
import com.gym.infrastructure.web.dto.auth.ChangePasswordRequest;
import com.gym.infrastructure.web.dto.auth.LoginRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationSteps {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TraineeJpaRepository traineeJpaRepository;
    private ResultActions result;

    @Given("a registered trainee with username {string} and password {string}")
    public void registeredTrainee(String username, String password) {
        UserEntity user = new UserEntity(
                "Alimardon",
                "Umidov",
                username,
                passwordEncoder.encode(password),
                true
        );

        TraineeEntity trainee = new TraineeEntity(null, null, user);
        traineeJpaRepository.save(trainee);
    }

    @Given("a user {string} exists with password {string}")
    public void userExistsWithPassword(String username, String password) {
        UserEntity user = new UserEntity(
                "Alimardon",
                "Umidov",
                username,
                passwordEncoder.encode(password),
                true
        );

        TraineeEntity trainee = new TraineeEntity(null, null, user);
        traineeJpaRepository.save(trainee);
    }

    @When("I login as {string} with password {string}")
    public void login(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest(username, password);

        result = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    @When("I change password from {string} to {string}")
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        LoginRequest loginRequest = new LoginRequest(
                "alimardon",
                oldPassword
        );

        String loginResponse = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(loginResponse)
                .get("token")
                .asText();

        ChangePasswordRequest request = new ChangePasswordRequest(
                "alimardon",
                oldPassword,
                newPassword
        );

        result = mockMvc.perform(
                put("/api/auth/password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        result.andExpect(status().is(expectedStatus));
    }

    @Then("an authentication token should be returned")
    public void authenticationTokenShouldBeReturned() throws Exception {
        String response = result.andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(response)
                .get("token")
                .asText();

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Then("the user should be able to login with {string}")
    public void userShouldBeAbleToLogin(String password) throws Exception {
        LoginRequest request = new LoginRequest(
                "alimardon",
                password
        );

        mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());
    }
}
