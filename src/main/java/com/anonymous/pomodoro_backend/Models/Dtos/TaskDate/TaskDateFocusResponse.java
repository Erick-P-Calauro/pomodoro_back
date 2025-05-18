package com.anonymous.pomodoro_backend.Models.Dtos.TaskDate;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDateFocusResponse {
    
    private UUID id;

    private Date date;

    private Time time; 

    private int duration;
}
