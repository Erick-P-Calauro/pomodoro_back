package com.anonymous.pomodoro_backend.Models;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
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
public class SettingsTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Min(value = 1)
    private int productivityTime;

    @NotNull
    @Min(value = 1)
    private int longRestTime;

    @NotNull
    @Min(value = 1)
    private int shortRestTime;

    @OneToOne(mappedBy="time")
    private Settings settings;

}
