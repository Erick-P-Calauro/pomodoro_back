package com.anonymous.pomodoro_backend.Controllers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
import org.springframework.web.server.ResponseStatusException;
import com.anonymous.pomodoro_backend.Errors.InputNotValidException;
import com.anonymous.pomodoro_backend.Errors.UserNotFoundException;
import com.anonymous.pomodoro_backend.Models.Role;
import com.anonymous.pomodoro_backend.Models.User;
import com.anonymous.pomodoro_backend.Models.Dtos.LoginResponse;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserCreate;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserEdit;
import com.anonymous.pomodoro_backend.Models.Dtos.User.UserResponse;
import com.anonymous.pomodoro_backend.Models.Mappers.UserMapper;
import com.anonymous.pomodoro_backend.Services.RoleService;
import com.anonymous.pomodoro_backend.Services.TaskService;
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

    @Autowired
    TaskService taskService;

    @Autowired
    JwtEncoder tokenEncoder;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    // Salvar usuário é livre
    @PostMapping("/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserCreate userCreate, BindingResult result) throws InputNotValidException {

        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorText = ErrorUtils.generateErrorMessage(errors);
            
            throw new InputNotValidException(errorText);
        }

        // 2L => ROLE USER (RolesConfiguration.java)
        Role userRole = roleService.getRole(2L);
        
        // Cryptography to user password
        userCreate.setPassword(passwordEncoder.encode(userCreate.getPassword()));

        User user = UserMapper.toEntity(userCreate, userRole);
        user = userService.saveUser(user);
        UserResponse userResponse = UserMapper.toResponse(user);
        
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserCreate userLogin) throws UserNotFoundException {
        
        User user = userService.getUserByUsername(userLogin.getUsername());

        if(!(passwordEncoder.matches(userLogin.getPassword(), user.getPassword()))) {
            throw new BadCredentialsException("username or password invalid.");
        }

        final Instant nowInstant = Instant.now();
        final Long expiresIn = 600L; 

        final String scope = user.getRoles().stream().map(role -> role.getName()).collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("pomodoro_backend") // Quem gerou token
            .subject(user.getId().toString()) // Para quem foi gerado o token
            .issuedAt(nowInstant) // Quando foi gerado
            .expiresAt(nowInstant.plusSeconds(expiresIn)) // Quando espira
            .claim("scope", scope) // Escopo (Roles)
            .build();
        
        String jwtValue = tokenEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

    @GetMapping("/list") // ("/")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<User> users = userService.listUsers();
        List<UserResponse> usersResponse = UserMapper.toListResponse(users);

        return ResponseEntity.ok(usersResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.ok("Usuário de id "+ id +" deletado com sucesso.");
    }

    @GetMapping("/")
    public ResponseEntity<UserResponse> getUserByToken(JwtAuthenticationToken token) throws UserNotFoundException {
        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);

        UserResponse userResponse = UserMapper.toResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}") 
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") UUID id, JwtAuthenticationToken token) throws UserNotFoundException {

        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);

        if(!id.equals(UUID.fromString(token.getName())) && !user.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        user = userService.getUser(id);
        UserResponse userResponse = UserMapper.toResponse(user);

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponse> editUser(@PathVariable("id") UUID id, @RequestBody @Valid UserEdit userEdit, 
        BindingResult result, JwtAuthenticationToken token) throws InputNotValidException, UserNotFoundException {
        
        if(result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            String errorText = ErrorUtils.generateErrorMessage(errors);
            
            throw new InputNotValidException(errorText);
        }

        UUID subjectId = UUID.fromString(token.getName());
        User user = userService.getUser(subjectId);

        if(!id.equals(subjectId) && !user.getUsername().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        User oldUser = userService.getUser(id);
        List<Role> roles = oldUser.getRoles();

        user = UserMapper.toEntity(userEdit, roles);
        user = userService.editUser(id, user);
        UserResponse userResponse = UserMapper.toResponse(user);

        return ResponseEntity.ok(userResponse);
    }

}
