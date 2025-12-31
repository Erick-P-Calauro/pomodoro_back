package com.anonymous.pomodoro_backend.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anonymous.pomodoro_backend.Errors.ProjectNotFoundException;
import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Repositories.ProjectRepository;
import com.anonymous.pomodoro_backend.Users.Models.User;

@Service
public class ProjectService {
    
    @Autowired
    ProjectRepository projectRepository;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProject(UUID id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);

        if(project.isEmpty()){
            throw new ProjectNotFoundException(id);
        }

        return project.get();
    }

    public List<Project> listProjects() {
        return projectRepository.findAll();
    }

    public List<Project> listProjectsByUser(User user) {
        return projectRepository.findByUser(user);
    }

    public Project editProject(UUID id, Project project) {
        project.setId(id);

        return projectRepository.save(project);
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }
}
