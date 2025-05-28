package com.anonymous.pomodoro_backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anonymous.pomodoro_backend.Errors.InputNotValidException;
import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Errors.UserNotFoundException;
import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.ProjectMapper;
import com.anonymous.pomodoro_backend.Services.ProjectService;
import com.anonymous.pomodoro_backend.Services.TaskService;
import com.anonymous.pomodoro_backend.Services.UserService;
import com.anonymous.pomodoro_backend.Utils.ErrorUtils;

import jakarta.validation.Valid;

@RequestMapping("/project")
@RestController
public class ProjectController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    ProjectService projectService;

    // Avaliar a necessidade de adicionar informações de focus no project
    @PostMapping("/save")
    public ResponseEntity<ProjectResponse> saveProject(@RequestBody @Valid ProjectCreate projectCreate, 
        BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException, TaskNotFoundException {
        
        if(result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();
            String errorMessage = ErrorUtils.generateErrorMessage(errors);

            throw new InputNotValidException(errorMessage);
        }

        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        List<UUID> tasksId = projectCreate.getTasksId();
        ArrayList<Task> tasks = new ArrayList<Task>();
        for(int i = 0; i < tasksId.size(); i++) {
            Task task = taskService.getTask(tasksId.get(i));
            tasks.add(task);
        }

        Project project = ProjectMapper.toEntity(projectCreate, tasks, subject);
        project = projectService.saveProject(project);
        ProjectResponse projectResponse = ProjectMapper.toDTO(project);

        return ResponseEntity.ok(projectResponse);
    }

}
