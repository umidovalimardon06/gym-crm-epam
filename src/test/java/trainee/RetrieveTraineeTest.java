package com.gym.application.usecase.trainee;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.TraineeRepository;
import com.gym.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveTraineeServiceTest {

    @Mock AuthenticateUseCase authenticator;
    @Mock TraineeRepository traineeRepository;

    @InjectMocks RetrieveTraineeService service;

    AuthCredentials auth;
    Trainee trainee;

    @BeforeEach
    void setUp() {
        auth = new AuthCredentials("Alimardon.Umidov", "123");
        trainee = new Trainee();
        trainee.setUsername("Alimardon.Umidov");
    }

    @Test
    void returns_trainee_by_username() {
        when(traineeRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(trainee));

        Trainee result = service.getTrainee(auth, "Alimardon.Umidov");

        assertThat(result.getUsername()).isEqualTo("Alimardon.Umidov");
    }

    @Test
    void throws_when_not_found() {
        when(traineeRepository.findByUsername("Missing.User")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getTrainee(auth, "Missing.User"))
                .isInstanceOf(NotFoundException.class);
    }
}