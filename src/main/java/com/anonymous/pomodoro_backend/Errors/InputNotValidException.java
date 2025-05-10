package com.anonymous.pomodoro_backend.Errors;

public class InputNotValidException extends Exception {
    
    public InputNotValidException(String text) {
        super(text);
    }

}
