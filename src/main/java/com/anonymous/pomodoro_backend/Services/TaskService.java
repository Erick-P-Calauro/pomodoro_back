package com.anonymous.pomodoro_backend.Services;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Repositories.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskDateService taskDateService;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTask(UUID id) {
        Task task = taskRepository.getReferenceById(id);

        return task;
    }

    public List<Task> listTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks;
    }

    public Task editTask(UUID id, Task newTask) {
        newTask.setId(id);
        
        return taskRepository.save(newTask);
    }

    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }

    public void addTaskDate(Date date, Time time, UUID taskId) {
         
    }

    public void deactivateTask(UUID id) {

    }

    public void addProductivityDone(UUID id) {

    }

    public float getHoursFocused(UUID id) {
        return 0.0f;
    }

    public int getDaysFocused(UUID id) {
        return 0;
    }

    public void getAllTaskDate(UUID id) {

    }
    
}
