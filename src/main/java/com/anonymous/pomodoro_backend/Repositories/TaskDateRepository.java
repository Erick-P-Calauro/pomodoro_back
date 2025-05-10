package com.anonymous.pomodoro_backend.Repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anonymous.pomodoro_backend.Models.TaskDate;

public interface TaskDateRepository extends JpaRepository<TaskDate, UUID> {
    
}
