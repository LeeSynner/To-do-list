package com.example.to_do_list.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
