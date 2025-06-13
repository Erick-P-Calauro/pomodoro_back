package com.anonymous.pomodoro_backend.Models.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectResponse;

public class ProjectMapper {
    
    public static Project toEntity(ProjectCreate projectCreate, User user) {
        Project project = new Project();
        project.setName(projectCreate.getName());
        project.setTasks(new ArrayList<Task>());
        project.setUser(user);

        return project;
    }

    public static ProjectResponse toDTO(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setTasks(TaskMapper.toListResponse(project.getTasks()));

        return response;
    }

    public static List<ProjectResponse> toListDTO(List<Project> projects) {
        List<ProjectResponse> projectsResponse = projects.stream()
            .map(project -> toDTO(project))
            .collect(Collectors.toList());

        return projectsResponse;
    }

    public static Project toEntity(ProjectEdit projectEdit, User subject) {
        Project project = new Project();
        project.setName(projectEdit.getName());
        project.setTasks(new ArrayList<Task>());
        project.setUser(subject);

        return project;
    }

}
