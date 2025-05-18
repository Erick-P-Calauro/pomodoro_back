package com.anonymous.pomodoro_backend.Repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anonymous.pomodoro_backend.Models.Task;
import com.anonymous.pomodoro_backend.Models.User;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByUser(User user);
}
