package com.anonymous.pomodoro_backend.Errors;

import java.util.UUID;

public class TaskDateNotFoundException extends Exception {
    
    public TaskDateNotFoundException(UUID id) {
        super("As informações de produtividade de tarefa com o id '" + id +"' não foram encontradas.");
    }

}
