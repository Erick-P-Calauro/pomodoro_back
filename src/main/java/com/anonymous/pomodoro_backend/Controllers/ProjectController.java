package com.anonymous.pomodoro_backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.anonymous.pomodoro_backend.Errors.InputNotValidException;
import com.anonymous.pomodoro_backend.Errors.ProjectNotFoundException;
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

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<ProjectResponse>> listProjects() throws UserNotFoundException{
        
        List<Project> projects = projectService.listProjects();
        List<ProjectResponse> projectResponse = ProjectMapper.toListDTO(projects);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping("/save")
    public ResponseEntity<ProjectResponse> saveProject(@RequestBody @Valid ProjectCreate projectCreate, 
        BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException, TaskNotFoundException, ProjectNotFoundException {
        
        if(result.hasErrors()) {

            List<ObjectError> errors = result.getAllErrors();
            String errorMessage = ErrorUtils.generateErrorMessage(errors);

            throw new InputNotValidException(errorMessage);
        }

        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        Project project = ProjectMapper.toEntity(projectCreate, subject);
        project = projectService.saveProject(project);
        
        List<UUID> tasksId = projectCreate.getTasksId();
        List<Task> tasks = new ArrayList<Task>();
        for(int i = 0; i < tasksId.size(); i++) {
            Task task = taskService.getTask(tasksId.get(i));
            task.setProject(project);
            taskService.saveTask(task);

            tasks.add(task);
        }

        project.setTasks(tasks);
        ProjectResponse projectResponse = ProjectMapper.toDTO(project);

        return ResponseEntity.ok(projectResponse);
    }

    @GetMapping("/{id}") // Teste
    public ResponseEntity<ProjectResponse> getProject(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException, ProjectNotFoundException {
        
        Project project = projectService.getProject(id);
        UUID projectUserId = project.getUser().getId();
        UUID subjectId = UUID.fromString(token.getName());

        if(!subjectId.equals(projectUserId) && !project.getUser().getUsername().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        ProjectResponse response = ProjectMapper.toDTO(project);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/{id}") // Teste
    public ResponseEntity<List<ProjectResponse>> listProjectsByUser(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException {
        
        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        if(!subjectId.equals(id) && !subject.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Project> projects = projectService.listProjectsByUser(id);
        List<ProjectResponse> response = ProjectMapper.toListDTO(projects);
       
        return ResponseEntity.ok(response);
    }

    @GetMapping("/focus/{id}")
    public ResponseEntity<ProjectResponse> getDetailedProject(){
        return ResponseEntity.ok(new ProjectResponse());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProjectResponse> editProject() {
        return ResponseEntity.ok(new ProjectResponse());
    }

    @PutMapping("/add/task/{id}")
    public ResponseEntity<ProjectResponse> addTaskOfProject() {
        return ResponseEntity.ok(new ProjectResponse());
    }

    @PutMapping("/delete/task/{id}")
    public ResponseEntity<ProjectResponse> deleteTaskofProject() {
        return ResponseEntity.ok(new ProjectResponse());
    }

    @DeleteMapping("/delete/{id}") // Teste
    public ResponseEntity<String> deleteProject(@PathVariable("id") UUID projectId, JwtAuthenticationToken token) throws UserNotFoundException, ProjectNotFoundException {
        
        Project project = projectService.getProject(projectId);
        
        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        if(!project.getUser().getId().equals(subjectId) && !subject.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        projectService.deleteProject(project);
        
        return ResponseEntity.ok("Projeto " + project.getName() + " deletado com sucesso.");
    }

}
