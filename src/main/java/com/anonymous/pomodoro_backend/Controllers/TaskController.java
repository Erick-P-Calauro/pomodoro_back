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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.TaskMapper;
import com.anonymous.pomodoro_backend.Services.TaskService;
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

        return ResponseEntity.accepted().body(tasksResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") UUID id) throws TaskNotFoundException {

        Task task = taskService.getTask(id);
        TaskResponse taskResponse = TaskMapper.toResponse(task);

        return ResponseEntity.accepted().body(taskResponse);
    }
    
    @PostMapping("/save")
    public ResponseEntity<Object> saveTask(@ModelAttribute("task") @Valid TaskCreate taskCreate, 
        BindingResult result) {

            if(result.hasErrors()) {

                List<String> errors = result.getAllErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .toList();

                return ResponseEntity.badRequest().body(errors);
            } // TODO : Melhorar formato do retorno do erro.

            Task task = TaskMapper.toEntity(taskCreate);
            task = taskService.saveTask(task);
            TaskResponse taskResponse = TaskMapper.toResponse(task);

            return ResponseEntity.accepted().body(taskResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Object> editTask(@PathVariable("id") UUID id, 
        @ModelAttribute @Valid TaskEdit taskEdit, BindingResult result) {

            if(result.hasErrors()) {

                List<String> errors = result.getAllErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .toList();

                return ResponseEntity.badRequest().body(errors);
            } // TODO : Melhorar formato do retorno do erro.

            Task task = TaskMapper.toEntity(taskEdit);
            task = taskService.editTask(id, task);
            TaskResponse taskResponse = TaskMapper.toResponse(task);

            return ResponseEntity.accepted().body(taskResponse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") UUID id) {
        taskService.deleteTask(id);

        return ResponseEntity.accepted().body("Tarefa deletada com sucesso");
    }

    // TODO : Se houver muito delay no tempo usar Date e Time recebidos no request
    @GetMapping("/addproductivity/{id}") 
    public ResponseEntity<String> addProcutivity(@PathVariable("id") UUID id) throws TaskNotFoundException {
        
        taskService.addProductivityDone(id);
        taskService.addTaskDate(Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), id);

        Task task = taskService.getTask(id);

        if(task.getProductivityDone() == task.getProductivityGoal()) {
            taskService.deactivateTask(id);
        }

        return ResponseEntity.accepted().body("Sessão de produtividade adicionada com sucesso.");
    }


}
