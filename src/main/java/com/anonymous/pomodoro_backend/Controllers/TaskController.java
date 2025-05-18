package com.anonymous.pomodoro_backend.Controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Errors.UserNotFoundException;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.ProductivityRequest;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskFocusResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskDate.TaskDateFocusResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.TaskDateMapper;
import com.anonymous.pomodoro_backend.Models.Mappers.TaskMapper;
import com.anonymous.pomodoro_backend.Services.TaskService;
import com.anonymous.pomodoro_backend.Services.UserService;
import com.anonymous.pomodoro_backend.Utils.ErrorUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<TaskResponse>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        List<TaskResponse> tasksResponse = TaskMapper.toListResponse(tasks);

        return ResponseEntity.ok(tasksResponse);
    }

    @GetMapping("/list") // Tasks
    public ResponseEntity<List<TaskResponse>> listTaskByUser() {
        return ResponseEntity.ok(new ArrayList<TaskResponse>());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws TaskNotFoundException, UserNotFoundException {

        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);

        Task task = taskService.getTask(id);

        if(!(task.getUser().getId().equals(subjectId)) && !user.getUsername().equals("admin")) { // Se token.id diferente de Task.user.id
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        TaskResponse taskResponse = TaskMapper.toResponse(task);

        return ResponseEntity.ok(taskResponse);
    }
    
    @PostMapping("/save")
    public ResponseEntity<TaskResponse> saveTask(@RequestBody @Valid TaskCreate taskCreate, 
        BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException, TaskNotFoundException {

        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorText = ErrorUtils.generateErrorMessage(errors);
            
            throw new InputNotValidException(errorText);
        }

        UUID subjectId = UUID.fromString(token.getName());
        User subject = userService.getUser(subjectId);

        Task task = TaskMapper.toEntity(taskCreate);
        task.setUser(subject);
        
        task = taskService.saveTask(task);
        if(task.getProductivityDone() != 0) {
            final int productivityDone = task.getProductivityDone();

            for(int i = 0 ; i < productivityDone; i++) {
                taskService.addTaskDate(Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), 30, task.getId());
            }

            if(task.getProductivityDone() == task.getProductivityGoal()) {
                taskService.deactivateTask(task.getId());
            }
        }

        TaskResponse taskResponse = TaskMapper.toResponse(task);

        return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("id") UUID id, 
        @RequestBody @Valid TaskEdit taskEdit, BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException, TaskNotFoundException {

            if(result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                String errorText = ErrorUtils.generateErrorMessage(errors);

                throw new InputNotValidException(errorText);
            }

            UUID subjectId = UUID.fromString(token.getName());
            User user = userService.getUser(subjectId);
            Task oldTask = taskService.getTask(id);

            if(!(oldTask.getUser().getId().equals(subjectId)) && !user.getUsername().equals("admin")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            Task task = TaskMapper.toEntity(taskEdit);
            task.setUser(user);

            task = taskService.editTask(id, task);
            TaskResponse taskResponse = TaskMapper.toResponse(task);

            return ResponseEntity.ok(taskResponse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException, TaskNotFoundException {
        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);
        Task task = taskService.getTask(id);

        if(!(task.getUser().getId().equals(subjectId)) && !user.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        
        taskService.deleteTask(id);

        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }

    @GetMapping("/productivity/{id}") 
    public ResponseEntity<String> addProcutivity(@PathVariable("id") UUID id, @RequestBody ProductivityRequest request, 
        BindingResult result, JwtAuthenticationToken token) throws TaskNotFoundException, UserNotFoundException {

        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);
        Task task = taskService.getTask(id);

        if(!(task.getUser().getId().equals(subjectId)) && !user.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        taskService.addProductivityDone(id);
        taskService.addTaskDate(Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), request.getMinutes(), id);

        task = taskService.getTask(id);

        if(task.getProductivityDone() == task.getProductivityGoal()) {
            taskService.deactivateTask(id);
        }

        return ResponseEntity.ok("Sessão de produtividade adicionada com sucesso.");
    }

    @GetMapping("/focus/list") // Tasks and Task Dates
    public ResponseEntity<List<TaskResponse>> listTaskAndDatesByUser() {
        return ResponseEntity.ok(new ArrayList<TaskResponse>());
    }

    @GetMapping("/focus/{id}")
    public ResponseEntity<TaskFocusResponse> getFocusByTask(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws TaskNotFoundException, UserNotFoundException {
        
        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);
        Task task = taskService.getTask(id);

        if(!(task.getUser().getId().equals(subjectId)) && !user.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<TaskDateFocusResponse> tasksResponse = TaskDateMapper.toFocusListResponse(taskService.getAllTaskDate(task.getId()));
        Float hours = taskService.getHoursFocused(id);
        Integer days = taskService.getDaysFocused(id);

        TaskFocusResponse response = new TaskFocusResponse(
            task.getId(), task.getTitle(), 
            task.getProductivityGoal(), task.getProductivityDone(), 
            tasksResponse, days, hours);

        return ResponseEntity.ok(response);
    }

}
