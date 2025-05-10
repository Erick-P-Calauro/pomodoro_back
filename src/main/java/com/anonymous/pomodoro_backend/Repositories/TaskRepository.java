package com.anonymous.pomodoro_backend.Repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anonymous.pomodoro_backend.Models.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    
}
