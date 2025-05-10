package com.anonymous.pomodoro_backend.Services;

import java.sql.Time;
import java.util.ArrayList;
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
        return new Task();
    }

    public Task getTask(UUID id) {
        return new Task();
    }

    public List<Task> listTasks() {
        return new ArrayList<Task>();
    }

    public Task editTask(UUID id, Task newTask) {
        return new Task();
    }

    public void deleteTask(UUID id) {

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
