package com.anonymous.pomodoro_backend.Repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Models.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByUser(User user);
}
