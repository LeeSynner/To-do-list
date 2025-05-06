package com.example.to_do_list.service;

import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.UserDto;
import com.example.to_do_list.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto create(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();
        return toDto(userRepository.save(user));
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
