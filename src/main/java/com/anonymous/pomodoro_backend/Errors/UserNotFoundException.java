package com.anonymous.pomodoro_backend.Errors;

import java.util.UUID;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(UUID id) {
        super("O usuário com id " + id.toString() + " não foi encontrado.");
    }

    public UserNotFoundException(String username) {
        super("O usuário com id " + username + " não foi encontrado.");
    }
}
