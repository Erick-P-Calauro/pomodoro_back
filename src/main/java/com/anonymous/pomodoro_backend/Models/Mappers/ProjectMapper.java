package com.anonymous.pomodoro_backend.Models.Mappers;

import java.util.List;

import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectResponse;

public class ProjectMapper {
    
    public static Project toEntity(ProjectCreate projectCreate, List<Task> tasks, User user) {
        Project project = new Project();
        project.setName(projectCreate.getName());
        project.setTasks(tasks);
        project.setUser(user);

        return project;
    }

    public static ProjectResponse toDTO(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setName(project.getName());
        response.setTasks(TaskMapper.toListResponse(project.getTasks()));

        return response;
    }

}
