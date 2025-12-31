package com.anonymous.pomodoro_backend.Users.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anonymous.pomodoro_backend.Users.Models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
