package com.anonymous.pomodoro_backend.Repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.TaskDate;

public interface TaskDateRepository extends JpaRepository<TaskDate, UUID> {
    List<TaskDate> findAllByTask(Task task);
}
