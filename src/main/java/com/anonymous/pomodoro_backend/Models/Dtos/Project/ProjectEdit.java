package com.anonymous.pomodoro_backend.Models.Dtos.Project;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEdit {
    
    @NotBlank
    private String name;

}
