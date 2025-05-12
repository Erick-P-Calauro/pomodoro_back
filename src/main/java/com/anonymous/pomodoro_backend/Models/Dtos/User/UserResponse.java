package com.anonymous.pomodoro_backend.Models.Dtos.User;

import java.util.List;
import java.util.UUID;
import com.anonymous.pomodoro_backend.Models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;

    private String username;

    private String password;

    private List<Role> roles;
    
}
