package com.anonymous.pomodoro_backend.Models.Dtos.Project;

import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreate {
    
    @NotBlank
    private String name;

    private List<UUID> tasksId;

}
