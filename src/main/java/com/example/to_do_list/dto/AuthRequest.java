package com.example.to_do_list.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull(message = "Логин - это обязательное поле")
    private String username;
    @NotNull(message = "Пароль - это обязательное поле")
    private String password;
}
