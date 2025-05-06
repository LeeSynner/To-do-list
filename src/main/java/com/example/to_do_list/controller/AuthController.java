package com.example.to_do_list.controller;

import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.AuthRequest;
import com.example.to_do_list.dto.AuthResponse;
import com.example.to_do_list.dto.UserDto;
import com.example.to_do_list.repository.UserRepository;
import com.example.to_do_list.service.JwtService;
import com.example.to_do_list.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public  AuthController(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("enter to /" +
                "auth/login");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        String token = jwtService.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userDto));
    }
}
