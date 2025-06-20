package com.anonymous.pomodoro_backend.Models.Dtos.Project;

import java.util.List;
import java.util.UUID;

import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    
    private UUID id;

    private String name;

    private List<TaskResponse> tasks;

}
