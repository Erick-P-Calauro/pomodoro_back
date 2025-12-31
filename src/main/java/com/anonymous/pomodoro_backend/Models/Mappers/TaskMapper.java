package com.anonymous.pomodoro_backend.Models.Mappers;

import java.util.ArrayList;
import java.util.List;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.TaskDate;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.Task.TaskResponse;
import com.anonymous.pomodoro_backend.Users.Models.User;
import com.anonymous.pomodoro_backend.Users.Models.DTO.User.UserResponse;

public class TaskMapper {

    public static Task toEntity(TaskCreate taskCreate) {
        Task task = new Task();
        
        task.setActive(true);
        task.setTitle(taskCreate.getTitle());
        task.setDescription(taskCreate.getDescription());
        task.setProductivityGoal(taskCreate.getProductivityGoal());
        task.setProductivityDone(taskCreate.getProductivityDone());
        task.setProject(null); // TODO : No-Project Project
        task.setTaskDates(new ArrayList<TaskDate>());

        return task;
    }

    public static Task toEntity(TaskEdit taskEdit) {
        Task task = new Task();
        
        task.setActive(taskEdit.isActive());
        task.setTitle(taskEdit.getTitle());
        task.setDescription(taskEdit.getDescription());
        task.setProductivityGoal(taskEdit.getProductivityGoal());
        task.setProductivityDone(taskEdit.getProductivityDone());

        return task;
    }

    public static TaskResponse toResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setProductivityGoal(task.getProductivityGoal());
        taskResponse.setProductivityDone(task.getProductivityDone());
        taskResponse.setActive(task.isActive());

        User user = task.getUser();

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setRoles(user.getRoles());

        taskResponse.setOwner(userResponse);

        return taskResponse;
    }

    public static List<TaskResponse> toListResponse(List<Task> tasks) {
        List<TaskResponse> tasksResponse = new ArrayList<TaskResponse>();

        tasksResponse = tasks.stream()
                            .map(task -> toResponse(task))
                            .toList();

        return tasksResponse;
    }
    
}
