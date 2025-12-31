package com.anonymous.pomodoro_backend.Models;

import java.util.List;
import java.util.UUID;

import com.anonymous.pomodoro_backend.Users.Models.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy="project", cascade = CascadeType.DETACH)
    private List<Task> tasks;

    @ManyToOne
    private User user;
    
}
