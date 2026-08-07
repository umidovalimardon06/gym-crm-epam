package com.gym.application.port.input.auth;

public interface ChangePasswordUseCase {
    void changePassword(String username, String oldPassword, String newPassword);
}