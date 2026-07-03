package com.gym.application.usecase.auth;

import com.gym.application.exception.NotFoundException;
import com.gym.application.port.input.auth.AuthCredentials;
import com.gym.application.port.input.auth.AuthenticateUseCase;
import com.gym.application.port.input.auth.ChangePasswordUseCase;
import com.gym.application.port.output.UserRepository;
import com.gym.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangePasswordService implements ChangePasswordUseCase {

    private final AuthenticateUseCase authenticator;
    private final UserRepository userRepository;

    public ChangePasswordService(AuthenticateUseCase authenticator,
                                 UserRepository userRepository) {
        this.authenticator = authenticator;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        validate(username, oldPassword, newPassword);

        authenticator.authenticate(new AuthCredentials(username, oldPassword));

        if (oldPassword.equals(newPassword)) {
            throw new IllegalArgumentException("New password must differ from old");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    private void validate(String username, String oldPassword, String newPassword) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
        if (oldPassword == null || oldPassword.isBlank())
            throw new IllegalArgumentException("oldPassword is required");
        if (newPassword == null || newPassword.isBlank())
            throw new IllegalArgumentException("newPassword is required");
        if (newPassword.length() < 8)
            throw new IllegalArgumentException("newPassword must be at least 8 characters");
    }
}