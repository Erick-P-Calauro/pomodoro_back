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
import com.anonymous.pomodoro_backend.Models.TaskDate;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectFocusResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.Project.ProjectResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskFocusResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskDate.TaskDateFocusResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.ProjectMapper;
import com.anonymous.pomodoro_backend.Models.Mappers.TaskDateMapper;
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

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException, ProjectNotFoundException {
        
        Project project = projectService.getProject(id);
        UUID projectUserId = project.getUser().getId();
        
        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        if(!subjectId.equals(projectUserId) && !subject.getUsername().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        ProjectResponse response = ProjectMapper.toDTO(project);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<ProjectResponse>> listProjectsByUser(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException {
        
        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        if(!subjectId.equals(id) && !subject.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Project> projects = projectService.listProjectsByUser(subject);
        List<ProjectResponse> response = ProjectMapper.toListDTO(projects);
       
        return ResponseEntity.ok(response);
    }

    @GetMapping("/focus/{projectId}")
    public ResponseEntity<ProjectFocusResponse> getDetailedProject(@PathVariable("projectId") UUID projectId, JwtAuthenticationToken token) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException{

        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        Project project = projectService.getProject(projectId);
        UUID projectUser = project.getUser().getId();

        if(!projectUser.equals(subjectId) && !subject.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Task> tasks = project.getTasks();

        List<TaskFocusResponse> tasksFocusResponse = new ArrayList<TaskFocusResponse>();
        float projectHoursFocused = 0.0f;
        int projectDaysFocused = 0;

        // For each task extract Hour and Days productivity
        // List of taskDate
        // Format to Response and store in tasksFocusResponse
        for(int i = 0; i < tasks.size(); i++){
            Task task = tasks.get(i);
            UUID id = task.getId();

            // Sum to project hours and days focused
            float taskHoursFocused = taskService.getHoursFocused(id);
            int taskDaysFocused = taskService.getDaysFocused(id);

            projectHoursFocused += taskHoursFocused;
            projectDaysFocused += taskDaysFocused;

            // Extracting list of taskDate of each task
            List<TaskDate> taskDate = task.getTaskDates();
            List<TaskDateFocusResponse> taskDateResponse = TaskDateMapper.toFocusListResponse(taskDate);

            // For each task return a task response
            TaskFocusResponse taskResponse = new TaskFocusResponse(
                id, task.getTitle(), task.getProductivityGoal(), 
                task.getProductivityDone(), 
                taskDateResponse, taskDaysFocused, taskHoursFocused);
            
            tasksFocusResponse.add(taskResponse);
        }

        ProjectFocusResponse response = new ProjectFocusResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setHoursFocused(projectHoursFocused);
        response.setDaysFocused(projectDaysFocused);
        response.setTasks(tasksFocusResponse);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ProjectResponse> editProject(@PathVariable("id") UUID id, @Valid @RequestBody ProjectEdit projectEdit, BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException, ProjectNotFoundException {
        
        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorMessage = ErrorUtils.generateErrorMessage(errors);

            throw new InputNotValidException(errorMessage);
        }

        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        Project project = ProjectMapper.toEntity(projectEdit, subject);

        if(!subjectId.equals(project.getId()) && !subject.getUsername().equals("admin")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Project oldProject = projectService.getProject(id);

        project.setTasks(oldProject.getTasks());
        project = projectService.editProject(oldProject.getId(), project);
        
        ProjectResponse response = ProjectMapper.toDTO(project);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/add/task/{taskId}")
    public ResponseEntity<ProjectResponse> addTaskOfProject(
        @PathVariable("projectId") UUID projectId, 
        @PathVariable("taskId") UUID taskId, JwtAuthenticationToken token) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException {

            UUID subjectId = UUID.fromString(token.getName());

            Project project = projectService.getProject(projectId);
            Task task = taskService.getTask(taskId);
            User subject = userService.getUser(subjectId);

            UUID projectUser = project.getUser().getId();
            UUID taskUser = task.getUser().getId();

            // Authorization by matching user of project, task and token.
            if(!projectUser.equals(subjectId) && !taskUser.equals(subjectId) && !subject.getUsername().equals("admin")){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            List<Task> newTasksList = project.getTasks();
            newTasksList.add(task);

            task.setProject(project);
            project.setTasks(newTasksList);

            taskService.saveTask(task);
            project = projectService.saveProject(project);

            ProjectResponse response = ProjectMapper.toDTO(project);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/delete/task/{taskId}")
    public ResponseEntity<ProjectResponse> deleteTaskofProject(
        @PathVariable("projectId") UUID projectId,
        @PathVariable("taskId") UUID taskId, JwtAuthenticationToken token) throws ProjectNotFoundException, TaskNotFoundException, UserNotFoundException {

            UUID subjectId = UUID.fromString(token.getName());

            Project project = projectService.getProject(projectId);
            Task task = taskService.getTask(taskId);
            User subject = userService.getUser(subjectId);

            UUID projectUser = project.getUser().getId();
            UUID taskUser = task.getUser().getId();

            // Authorization by matching user of project, task and token.
            if(!projectUser.equals(subjectId) && !taskUser.equals(subjectId) && !subject.getUsername().equals("admin")){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            List<Task> newTasksList = project.getTasks();
            newTasksList.remove(task);

            task.setProject(null);
            project.setTasks(newTasksList);

            taskService.saveTask(task);
            project = projectService.saveProject(project);

            ProjectResponse response = ProjectMapper.toDTO(project);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
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
