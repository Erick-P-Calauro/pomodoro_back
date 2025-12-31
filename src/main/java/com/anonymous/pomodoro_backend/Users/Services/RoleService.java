package com.anonymous.pomodoro_backend.Users.Services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anonymous.pomodoro_backend.Users.Models.Role;
import com.anonymous.pomodoro_backend.Users.Repositories.RoleRepository;

@Service
public class RoleService {
    
    @Autowired
    RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).get();
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

}
