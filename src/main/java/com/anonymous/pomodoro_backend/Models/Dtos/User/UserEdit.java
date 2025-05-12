package com.anonymous.pomodoro_backend.Models.Dtos.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEdit {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
}
