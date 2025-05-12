package com.anonymous.pomodoro_backend.Models.Dtos.Task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreate {
    
    @NotBlank(message = "title não deve estar em branco")
    private String title;

    private String description;

    @NotNull(message = "productivityGoal não deve ser nulo")
    @Min(value=1, message = "productivityGoal deve ser maior que zero")
    private int productivityGoal;

    @NotNull(message = "productivityDone não deve ser nulo")
    private int productivityDone;
}
