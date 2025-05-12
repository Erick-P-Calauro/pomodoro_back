package com.anonymous.pomodoro_backend.Configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.anonymous.pomodoro_backend.Models.Role;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Services.RoleService;
import com.anonymous.pomodoro_backend.Services.UserService;

import jakarta.transaction.Transactional;

@Component
@Transactional
public class RolesConfiguration implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if(roleService.listRoles().size() == 0) {
            Role ADMIN_ROLE = new Role(1L, "ADMIN");
            Role USER = new Role(2L, "USER");

            ADMIN_ROLE = roleService.saveRole(ADMIN_ROLE);
            roleService.saveRole(USER);

            User ADMIN_USER = new User();
            ADMIN_USER.setUsername("admin");
            ADMIN_USER.setPassword(passwordEncoder.encode("admin"));
            
            ArrayList<Role> roles = new ArrayList<Role>();
            roles.add(ADMIN_ROLE);

            ADMIN_USER.setRoles(roles);

            userService.saveUser(ADMIN_USER);
        }

    }
    
}
