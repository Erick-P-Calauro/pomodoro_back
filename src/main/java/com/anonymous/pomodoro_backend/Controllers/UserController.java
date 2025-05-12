package com.anonymous.pomodoro_backend.Controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anonymous.pomodoro_backend.Errors.InputNotValidException;
import com.anonymous.pomodoro_backend.Errors.UserNotFoundException;
import com.anonymous.pomodoro_backend.Models.Role;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.UserMapper;
import com.anonymous.pomodoro_backend.Services.RoleService;
import com.anonymous.pomodoro_backend.Services.UserService;
import com.anonymous.pomodoro_backend.Utils.ErrorUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<User> users = userService.listUsers();
        List<UserResponse> usersResponse = UserMapper.toListResponse(users);

        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") UUID id) throws UserNotFoundException {

        User user = userService.getUser(id);
        UserResponse userResponse = UserMapper.toResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserCreate userCreate, BindingResult result) throws InputNotValidException {

        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorText = ErrorUtils.generateErrorMessage(errors);
            
            throw new InputNotValidException(errorText);
        }

        // 2L => ROLE USER (RolesConfiguration.java)
        Role userRole = roleService.getRole(2L);

        User user = UserMapper.toEntity(userCreate, userRole);
        user = userService.saveUser(user);
        UserResponse userResponse = UserMapper.toResponse(user);
        
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") UUID id, @RequestBody @Valid UserEdit userEdit, BindingResult result) throws InputNotValidException, UserNotFoundException {
        
        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorText = ErrorUtils.generateErrorMessage(errors);
            
            throw new InputNotValidException(errorText);
        }

        User oldUser = userService.getUser(id);
        List<Role> roles = oldUser.getRoles();

        User user = UserMapper.toEntity(userEdit, roles);
        user = userService.editUser(id, user);
        UserResponse userResponse = UserMapper.toResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.ok("Usuário de id "+ id +" deletado com sucesso.");
    }

}
