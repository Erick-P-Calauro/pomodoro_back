package com.anonymous.pomodoro_backend.Users.Models;

import java.util.List;
import java.util.UUID;

import com.anonymous.pomodoro_backend.Models.Project;
import com.anonymous.pomodoro_backend.Models.Task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tb_user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique=true)
    private String username;

    @NotBlank
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name="user_roles", 
        joinColumns = @JoinColumn(name="user_id"), 
        inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> projects;

}
