package com.anonymous.pomodoro_backend.Services;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anonymous.pomodoro_backend.Errors.TaskNotFoundException;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.TaskDate;
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

    public Task getTask(UUID id) throws TaskNotFoundException {
        Optional<Task> task = taskRepository.findById(id);

        if(task.isEmpty()){
            throw new TaskNotFoundException(id);
        }

        return task.get();
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

    public TaskDate addTaskDate(Date date, Time time, int duration, UUID taskId) throws TaskNotFoundException {
         
        Task task = getTask(taskId);
        
        TaskDate taskDate = new TaskDate();
        taskDate.setDate(date);
        taskDate.setTime(time);
        taskDate.setDuration(duration);
        taskDate.setTask(task);

        taskDate = taskDateService.saveTaskDate(taskDate);

        return taskDate;
    }

    public Task deactivateTask(UUID id) throws TaskNotFoundException {

        Task task = getTask(id);
        task.setActive(false);

        taskRepository.save(task);

        return task;
    }

    public Task addProductivityDone(UUID id) throws TaskNotFoundException {
        Task task = getTask(id);
        task.setProductivityDone(task.getProductivityDone() + 1);

        return taskRepository.save(task);
    }

    public List<TaskDate> getAllTaskDate(UUID id) throws TaskNotFoundException {
        Task task = getTask(id);

        return taskDateService.getTaskDateByTask(task);
    }

    public float getHoursFocused(UUID id) throws TaskNotFoundException {
        List<TaskDate> tasksDate = getAllTaskDate(id);

        float hoursFocused = 0.0f;
        for(int i = 0; i < tasksDate.size(); i++) {
            TaskDate task = tasksDate.get(i);

            hoursFocused += task.getDuration();
        }

        return hoursFocused/60;
    }

    public int getDaysFocused(UUID id) throws TaskNotFoundException {
        List<TaskDate> tasksDate = getAllTaskDate(id);

        int daysFocused = 0;
        int i = 0;

        if(tasksDate.size() == 1) {
            daysFocused = 1;
        }else if(tasksDate.size() > 1) {
            daysFocused = 1;

            for(int j = 0; j < tasksDate.size(); j++) {
                // Quando achar um diferente quer dizer que teve mais um dia de foco
                if(!(tasksDate.get(i).getDate().equals(tasksDate.get(j).getDate()))) {
                    i = j;
                    daysFocused += 1;
                }
            }
        }

        return daysFocused;
    }
    
}
