package com.anonymous.pomodoro_backend.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.TaskDate;
import com.anonymous.pomodoro_backend.Repositories.TaskDateRepository;

@Service
public class TaskDateService {

    @Autowired
    TaskDateRepository taskDateRepository;

    public TaskDate saveTaskDate(TaskDate taskDate) {
        return new TaskDate();
    }

    public TaskDate getTaskDate(UUID id) {
        return new TaskDate();
    }

    public List<TaskDate> getTaskDateByTask(Task task) {
        return new ArrayList<TaskDate>();
    }

    public void deleteTaskDate(UUID id) {
        
    }
    
}
