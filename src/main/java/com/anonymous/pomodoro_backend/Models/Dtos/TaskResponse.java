package com.anonymous.pomodoro_backend.Models.Dtos;

import java.util.UUID;

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

}
