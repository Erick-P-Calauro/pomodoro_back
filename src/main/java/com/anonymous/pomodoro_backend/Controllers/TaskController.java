package com.anonymous.pomodoro_backend.Controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.anonymous.pomodoro_backend.Errors.InputNotValidException;
import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.Dtos.ProductivityRequest;
import com.anonymous.pomodoro_backend.Models.Dtos.TimeInfoResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.TaskMapper;
import com.anonymous.pomodoro_backend.Services.TaskService;
import com.anonymous.pomodoro_backend.Utils.ErrorUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<TaskResponse>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        List<TaskResponse> tasksResponse = TaskMapper.toListResponse(tasks);

        return ResponseEntity.ok(tasksResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") UUID id) throws TaskNotFoundException {

        Task task = taskService.getTask(id);
        TaskResponse taskResponse = TaskMapper.toResponse(task);

        return ResponseEntity.ok(taskResponse);
    }
    
    @PostMapping("/save")
    public ResponseEntity<TaskResponse> saveTask(@RequestBody @Valid TaskCreate taskCreate, 
        BindingResult result) throws InputNotValidException {

            if(result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                String errorText = ErrorUtils.generateErrorMessage(errors);
                
                throw new InputNotValidException(errorText);
            }

            Task task = TaskMapper.toEntity(taskCreate);
            task = taskService.saveTask(task);
            TaskResponse taskResponse = TaskMapper.toResponse(task);

            return ResponseEntity.ok(taskResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("id") UUID id, 
        @RequestBody @Valid TaskEdit taskEdit, BindingResult result) throws InputNotValidException {

            if(result.hasErrors()) {
                List<ObjectError> errors = result.getAllErrors();
                String errorText = ErrorUtils.generateErrorMessage(errors);

                throw new InputNotValidException(errorText);
            }

            Task task = TaskMapper.toEntity(taskEdit);
            task = taskService.editTask(id, task);
            TaskResponse taskResponse = TaskMapper.toResponse(task);

            return ResponseEntity.ok(taskResponse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") UUID id) {
        taskService.deleteTask(id);

        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }

    @GetMapping("/productivity/{id}") 
    public ResponseEntity<String> addProcutivity(@PathVariable("id") UUID id, @RequestBody ProductivityRequest request, 
        BindingResult result) throws TaskNotFoundException {

        taskService.addProductivityDone(id);
        taskService.addTaskDate(Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), request.getMinutes(), id);

        Task task = taskService.getTask(id);

        if(task.getProductivityDone() == task.getProductivityGoal()) {
            taskService.deactivateTask(id);
        }

        return ResponseEntity.ok("Sessão de produtividade adicionada com sucesso.");
    }

    @GetMapping("/focus/{id}")
    public ResponseEntity<TimeInfoResponse> getHoursFocused(@PathVariable("id") UUID id) throws TaskNotFoundException {
        Float hours = taskService.getHoursFocused(id);
        Integer days = taskService.getDaysFocused(id);

        return ResponseEntity.ok(new TimeInfoResponse(days, hours));
    }


}
