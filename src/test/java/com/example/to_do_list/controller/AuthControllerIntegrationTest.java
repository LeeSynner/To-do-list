package com.example.to_do_list.controller;

import com.example.to_do_list.domain.Role;
import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.AuthRequest;
import com.example.to_do_list.dto.UserDto;
import com.example.to_do_list.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @ParameterizedTest
    @CsvSource({
            "testusername1, testpassword1, ROLE_USER",
            "testusername2, testpassword2, ROLE_USER",
            "testusername3, testpassword3, ROLE_ADMIN"
    })
    void testRegister_success(String username, String password, String role) throws Exception {
        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .role(Role.valueOf(role)).build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.role").value(role));
    }

    @ParameterizedTest
    @CsvSource({
            "testusername, , ROLE_USER",
            ", testpassword, ROLE_ADMIN",
            "testusername, testpassword, "
    })
    void testRegister_failed(String username, String password, String role) throws Exception {
        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .role(role == null || role.isEmpty() ? null : Role.valueOf(role)).build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "testusername, testpassword, ROLE_USER",
            "testusername123, testpassword123, ROLE_ADMIN"
    })
    void testRegister_whenUsernameAlreadyExists(String username, String password, String role) throws Exception {
        userRepository.save(User.builder()
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .role(Role.valueOf(role)).build());

        UserDto userDto = UserDto.builder()
                .username(username)
                .password(password)
                .role(Role.valueOf(role))
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRegister_whenBodyIsMissing() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "testusername, testpassword, ROLE_USER"
    })
    void testLogin_success(String username, String password, String role) throws Exception {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role))
                .build();
        userRepository.save(user);

        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "testusername123, password123",
            "testusername321, password321"
    })
    void testLogin_failed(String username, String password) throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password).build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource({
            "testusername123, ",
            ", password321",
            ",",
            "'', password123",
            "testusername543, ''",
            "'', 'testLogin'"
    })
    void testLogin_whenInvalidData(String username, String password) throws Exception {
        AuthRequest authRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest());
    }
}
