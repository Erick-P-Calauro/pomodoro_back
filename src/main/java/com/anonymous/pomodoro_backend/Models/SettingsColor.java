package com.anonymous.pomodoro_backend.Models;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingsColor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 7)
    private String productivityColor;

    @NotBlank
    @Size(max = 7)
    private String longRestColor;

    @NotBlank
    @Size(max = 7)
    private String shortRestColor;

    @OneToOne(mappedBy="colors")
    private Settings settings;

}
