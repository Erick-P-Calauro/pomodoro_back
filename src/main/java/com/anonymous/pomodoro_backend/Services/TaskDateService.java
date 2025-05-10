package com.anonymous.pomodoro_backend.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.TaskDate;
import com.anonymous.pomodoro_backend.Repositories.TaskDateRepository;

@Service
public class TaskDateService {

    @Autowired
    TaskDateRepository taskDateRepository;

    public TaskDate saveTaskDate(TaskDate taskDate) {
        return taskDateRepository.save(taskDate);
    }

    public TaskDate getTaskDate(UUID id) throws TaskNotFoundException {
        Optional<TaskDate> taskDate = taskDateRepository.findById(id);

        if(taskDate.isEmpty()){
            throw new TaskNotFoundException(id);
        }

        return taskDate.get();
    }

    public List<TaskDate> getTaskDateByTask(Task task) {
        return taskDateRepository.findAllByTask(task);
    }
    
}
