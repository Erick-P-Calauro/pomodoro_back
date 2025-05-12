package com.anonymous.pomodoro_backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.anonymous.pomodoro_backend.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
