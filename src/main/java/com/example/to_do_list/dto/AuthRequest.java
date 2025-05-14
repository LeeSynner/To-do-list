package com.example.to_do_list.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequest {
    @NotNull(message = "Логин - это обязательное поле")
    @NotEmpty
    private String username;
    @NotEmpty
    @NotNull(message = "Пароль - это обязательное поле")
    private String password;
}
