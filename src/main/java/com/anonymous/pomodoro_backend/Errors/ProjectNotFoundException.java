package com.anonymous.pomodoro_backend.Errors;

import java.util.UUID;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(UUID id) {
        super("O projeto com o id '" + id +"' não foi encontrado.");
    }
}
