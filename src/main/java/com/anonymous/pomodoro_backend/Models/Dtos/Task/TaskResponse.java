package com.anonymous.pomodoro_backend.Models.Dtos.Task;

import java.util.UUID;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    
    private UUID id;

    private String title;

    private String description;

    private int productivityGoal;

    private int productivityDone;

    private boolean active;

    private UserResponse owner;

}
