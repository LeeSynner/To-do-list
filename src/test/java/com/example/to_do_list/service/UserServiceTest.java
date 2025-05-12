package com.example.to_do_list.service;

import com.example.to_do_list.domain.Role;
import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.UserDto;
import com.example.to_do_list.exception.ConflictDataException;
import com.example.to_do_list.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_whenUserDoesNotExists() {
        UserDto userDto = UserDto.builder()
                .username("testUser")
                .password("password")
                .role(Role.ROLE_USER).build();

        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDto createdUser = userService.create(userDto);

         assertThat(createdUser).isNotNull();
         assertThat(createdUser.getUsername()).isEqualTo(userDto.getUsername());
         assertThat(createdUser.getPassword()).isNull();
         assertThat(createdUser.getRole()).isEqualTo(userDto.getRole());

         Mockito.verify(userRepository, Mockito.times(1))
                 .save(Mockito.any(User.class));
    }

    @Test
    void testCreateUser_whenUsernameAlreadyExists() {
        UserDto userDto = UserDto.builder()
                .username("testExistsUser")
                .password("password")
                .role(Role.ROLE_USER).build();

        Mockito.when(userRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.create(userDto))
                .isInstanceOf(ConflictDataException.class)
                .hasMessageContaining("Пользователь с таким username уже существует");
    }

    @Test
    void testGetIdByUsername_whenUserExists() {
        final String username = "testUser";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(User.builder().id(1L).build()));

        assertThatCode(() -> userService.getIdByUsername(username))
                .doesNotThrowAnyException();
    }

    @Test
    void testGetIdByUsername_whenUserDoesNotExists() {
        final String username = "testDoesNotExistsUsername";
        Mockito.when(userRepository.findByUsername((username)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getIdByUsername(username))
                .isInstanceOf(Throwable.class);
    }
}
