package com.anonymous.pomodoro_backend.Models.Mappers;

import java.util.ArrayList;
import java.util.List;
import com.anonymous.pomodoro_backend.Models.TaskDate;
import com.anonymous.pomodoro_backend.Models.Dtos.TaskDate.TaskDateFocusResponse;

public class TaskDateMapper {
 
    public static TaskDateFocusResponse toFocusResponse(TaskDate entity) {
        TaskDateFocusResponse DTO = new TaskDateFocusResponse();
        DTO.setDate(entity.getDate());
        DTO.setDuration(entity.getDuration());
        DTO.setId(entity.getId());
        DTO.setTime(entity.getTime());

        return DTO;
    }

    public static List<TaskDateFocusResponse> toFocusListResponse(List<TaskDate> listEntity) {
        List<TaskDateFocusResponse> listDTO = new ArrayList<TaskDateFocusResponse>();
        listDTO = listEntity.stream().map(taskDate -> toFocusResponse(taskDate)).toList();

        return listDTO;
    }

}
