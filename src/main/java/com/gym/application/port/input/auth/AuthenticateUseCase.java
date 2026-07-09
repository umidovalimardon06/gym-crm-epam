package com.gym.application.port.input.auth;

public interface AuthenticateUseCase {
    void authenticate(AuthCredentials credentials);
    void verifyCredentials(AuthCredentials credentials);
}