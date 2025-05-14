package com.example.to_do_list.controller;

import com.example.to_do_list.domain.Role;
import com.example.to_do_list.domain.Task;
import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.repository.TaskRepository;
import com.example.to_do_list.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private List<Task> listOfTasks;


    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder()
                .username("testuser")
                .password("testuser")
                .role(Role.ROLE_USER)
                .build());

        listOfTasks = taskRepository.saveAll(Arrays.asList(
                Task.builder()
                        .title("test1 title")
                        .description("test1 description")
                        .isCompleted(false)
                        .user(User.builder().id(user.getId()).build())
                        .build(),
                Task.builder()
                        .title("test2 title")
                        .description("test2 description")
                        .isCompleted(true)
                        .user(User.builder().id(user.getId()).build())
                        .build())
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
            "test title, test description, false",
            "test2 title, null, true",
            "test3 title, test3 description, true",
            "test3 title, null, false"
    }, nullValues = "null")
    @WithMockUser(username = "testuser")
    void testCreateTask_success(String title, String description, boolean completed) throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title(title)
                .description(description)
                .isCompleted(completed)
                .userId(user.getId())
                .build();

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.completed").value(completed));
    }

    @ParameterizedTest
    @CsvSource({
            "test description, true",
            "test description, false",
            ", false",
            ", true",
    })
    @WithMockUser(username = "testuser")
    void testCreateTask_failed(String description, boolean completed) throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .description(description)
                .isCompleted(completed)
                .build();

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTask_withoutAuth() throws Exception {
        TaskDto taskDto = TaskDto.builder().build();

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @CsvSource({
            "test1 title, test1 description, false",
            "test2 title, test2 description, true",
            "test3 title, , false",
            "test4 title, , true"
    })
    @WithMockUser(username = "testuser")
    void testUpdateTask(String title, String description, boolean completed) throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title(title)
                .description(description)
                .isCompleted(completed)
                .userId(user.getId()).build();
        long taskId = listOfTasks.get(0).getId();

        mockMvc.perform(put("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(jsonPath("$.completed").value(taskDto.isCompleted()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateTask_whenTaskDoesNotExists() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title("test title")
                .userId(user.getId()).build();
        long taskId = Long.MAX_VALUE;

        mockMvc.perform(put("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTask_withoutAuth() throws Exception {
        TaskDto taskDto = TaskDto.builder()
                .title("test title")
                .userId(user.getId()).build();
        long taskId = listOfTasks.get(0).getId();

        mockMvc.perform(put("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTasks_success() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("test1 title"))
                .andExpect(jsonPath("$[1].title").value("test2 title"));
    }

    @Test
    void testGetTasks_withoutAuth() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTask_success() throws Exception {
        Task task = listOfTasks.get(0);
        mockMvc.perform(get("/api/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.completed").value(task.isCompleted()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetTask_whenTaskDoesNotExists() throws Exception {
        long taskId = Long.MAX_VALUE;
        mockMvc.perform(get("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTask_withoutAuth() throws Exception {
        long taskId = listOfTasks.get(0).getId();
        mockMvc.perform(get("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteTask_whenTaskExists() throws Exception {
        long taskId = listOfTasks.get(0).getId();
        mockMvc.perform(delete("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testDeleteTask_whenTaskDoesNotExists() throws Exception {
        long taskId = Long.MAX_VALUE;
        mockMvc.perform(delete("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTask_withoutAuth() throws Exception {
        long taskId = listOfTasks.get(0).getId();
        mockMvc.perform(delete("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
