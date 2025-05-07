package com.example.to_do_list.service;

import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.UserDto;
import com.example.to_do_list.repository.UserRepository;
import com.example.to_do_list.service.interfaces.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Registration new user: {}", userDto.getUsername());
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();
        return toDto(userRepository.save(user));
    }
}
