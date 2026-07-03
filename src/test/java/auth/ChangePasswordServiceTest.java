package auth;

import com.gym.application.exception.AuthenticationException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.output.UserRepository;
import com.gym.application.usecase.auth.ChangePasswordService;
import com.gym.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {

    @Mock AuthenticateUseCase authenticator;
    @Mock UserRepository userRepository;

    @InjectMocks
    ChangePasswordService service;

    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("Alimardon.Umidov");
        user.setPassword("oldPassword");
        user.setActive(true);
    }

    @Test
    void changes_password_successfully() {
        when(userRepository.findByUsername("Alimardon.Umidov")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        service.changePassword("Alimardon.Umidov", "oldPassword", "newPassword");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("newPassword");
    }

    @Test
    void throws_on_wrong_old_password() {
        doThrow(new AuthenticationException("Invalid credentials"))
                .when(authenticator).authenticate(any(AuthCredentials.class));

        assertThatThrownBy(() ->
                service.changePassword("Alimardon.Umidov", "wrongPassword", "newPassword")
        ).isInstanceOf(AuthenticationException.class);
    }
}