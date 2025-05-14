package com.anonymous.pomodoro_backend.Models;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private int productivityDone;

    @NotNull
    private int productivityGoal;

    @NotNull
    private boolean active;

    @ManyToOne
    @JoinColumn(name="project")
    private Project project;

    @OneToMany(mappedBy="task", cascade = CascadeType.ALL)
    private List<TaskDate> taskDates;

    @ManyToOne
    private User user;
}
