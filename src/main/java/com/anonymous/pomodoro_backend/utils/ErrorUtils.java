package com.anonymous.pomodoro_backend.utils;
import java.util.List;

import org.springframework.validation.ObjectError;

public class ErrorUtils {
    
    public static String generateErrorMessage(List<ObjectError> errors) {
        String errorText = "";

        for(int i = 0; i < errors.size(); i++) {
            ObjectError error = errors.get(i);

            String errorMessage = error.getCode() + ":" + error.getDefaultMessage();
            errorText += errorMessage + " ";
        }

        return errorText;
    }

}
