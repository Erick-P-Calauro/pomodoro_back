package com.anonymous.pomodoro_backend.Models.Mappers;

import java.util.ArrayList;
import java.util.List;

import com.anonymous.pomodoro_backend.Models.Role;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserResponse;

public class UserMapper {

    public static User toEntity(UserCreate userCreate, Role userRole) {
        User user = new User();
        user.setUsername(userCreate.getUsername());
        user.setPassword(userCreate.getPassword());
        
        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(userRole);

        user.setRoles(roles);

        return user;
    }

    public static User toEntity(UserEdit userEdit, List<Role> userRoles) {
        User user = new User();
        user.setUsername(userEdit.getUsername());
        user.setPassword(userEdit.getPassword());

        user.setRoles(userRoles);

        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setPassword(user.getPassword());
        userResponse.setRoles(user.getRoles());

        return userResponse;
    }

    public static List<UserResponse> toListResponse(List<User> users) {
        List<UserResponse> usersResponse = users.stream()
                            .map(user -> toResponse(user))
                            .toList();
        
        return usersResponse;
    }
    
}
