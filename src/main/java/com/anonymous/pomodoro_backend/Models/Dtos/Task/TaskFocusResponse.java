package com.anonymous.pomodoro_backend.Models.Dtos.Task;

import java.util.List;
import java.util.UUID;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskDate.TaskDateFocusResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskFocusResponse {

    private UUID taskId;

    private String taskName;

    private int productivityGoal;

    private int productivityDone;

    private List<TaskDateFocusResponse> taskDates;

    private int daysFocused;

    private float hoursFocused;
 
}
