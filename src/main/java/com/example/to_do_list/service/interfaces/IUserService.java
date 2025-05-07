package com.example.to_do_list.service.interfaces;

import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.UserDto;

public interface IUserService {

    UserDto create(UserDto userDto);

    default UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
