package com.anonymous.pomodoro_backend.Errors;

import java.util.UUID;

public class TaskNotFoundException extends Exception {
    
    public TaskNotFoundException(UUID id) {
        super("A tarefa com o id '" + id +"' não foi encontrada.");
    }
}
