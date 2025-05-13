package com.example.to_do_list.service;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetByUsername_whenTasksExists() {
        String username = "username";
        List<Task> list = new ArrayList<>();
        list.add(Task.builder()
                .title("first title")
                .description("first description")
                .isCompleted(false)
                .build());
        list.add(Task.builder()
                .title("second title")
                .description("second description")
                .isCompleted(true)
                .build());
        Mockito.when(taskRepository.findByUserUsername(username))
                .thenReturn(list);

        List<TaskDto> responseList = taskService.getByUsername(username);

        assertThat(responseList).isNotNull();
        assertThat(responseList.size()).isEqualTo(list.size());

        assertThat(responseList).extracting(TaskDto::getTitle)
                .containsExactly("first title", "second title");
        assertThat(responseList).extracting(TaskDto::getDescription)
                .containsExactly("first description", "second description");
        assertThat(responseList).extracting(TaskDto::isCompleted)
                .containsExactly(false, true);

        Mockito.verify(taskRepository, Mockito.times(1))
                .findByUserUsername(username);
    }

    @Test
    void testGetByUsername_whenTasksDoesNotExists() {
        String username = "username";
        Mockito.when(taskRepository.findByUserUsername(username))
                .thenReturn(new ArrayList<>());

        List<TaskDto> responseList = taskService.getByUsername(username);

        assertThat(responseList).isNotNull();
        assertThat(responseList).isEmpty();

        Mockito.verify(taskRepository, Mockito.times(1))
                .findByUserUsername(username);
    }

    @Test
    void testGetById_whenTaskExists() {
        Long id = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Task task = Task.builder()
                .title("test task title")
                .description("test task description")
                .dueDate(LocalDateTime.parse("2025-06-10 17:50:00", formatter))
                .isCompleted(false)
                .user(User.builder().id(1L).build())
                .build();

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        Optional<TaskDto> taskDto = taskService.getById(id);

        assertThat(taskDto).isPresent();
        assertThat(taskDto.get().getTitle()).isEqualTo(task.getTitle());
        assertThat(taskDto.get().getDescription()).isEqualTo(task.getDescription());
        assertThat(taskDto.get().getDueDate()).isEqualTo(task.getDueDate());
        assertThat(taskDto.get().isCompleted()).isEqualTo(task.isCompleted());
        assertThat(taskDto.get().getUserId()).isEqualTo(task.getUser().getId());

        Mockito.verify(taskRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void testGetById_whenTaskDoesNotExists() {
        Long id = 999L;
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());
        Optional<TaskDto> taskDto = taskService.getById(id);

        assertThat(taskDto.isEmpty()).isTrue();

        Mockito.verify(taskRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void testCreate() {
        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        final TaskDto taskDto = createTestTaskDto();

        final TaskDto createdTaskDto = taskService.create(taskDto);

        assertThat(createdTaskDto).isNotNull();
        assertThat(createdTaskDto.getTitle()).isEqualTo(taskDto.getTitle());
        assertThat(createdTaskDto.getDescription()).isEqualTo(taskDto.getDescription());
        assertThat(createdTaskDto.getDueDate()).isEqualTo(taskDto.getDueDate());
        assertThat(createdTaskDto.isCompleted()).isEqualTo(taskDto.isCompleted());
        assertThat(createdTaskDto.getUserId()).isEqualTo(taskDto.getUserId());

        Mockito.verify(taskRepository, Mockito.times(1))
                .save(Mockito.any(Task.class));
    }

    @Test
    void testUpdate_whenTaskExists() {
        Long id = 1L;
        TaskDto taskDto = createTestTaskDto();

        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .isCompleted(taskDto.isCompleted())
                .user(User.builder().id(taskDto.getUserId()).build())
                .build();

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        Optional<TaskDto> updatedTaskDto = taskService.update(id, taskDto);

        assertThat(updatedTaskDto).isPresent();
        assertThat(updatedTaskDto.get().getTitle()).isEqualTo(taskDto.getTitle());
        assertThat(updatedTaskDto.get().getDescription()).isEqualTo(taskDto.getDescription());
        assertThat(updatedTaskDto.get().getDueDate()).isEqualTo(taskDto.getDueDate());
        assertThat(updatedTaskDto.get().isCompleted()).isEqualTo(taskDto.isCompleted());
        assertThat(updatedTaskDto.get().getUserId()).isEqualTo(taskDto.getUserId());

        Mockito.verify(taskRepository, Mockito.times(1))
                .findById(id);
        Mockito.verify(taskRepository, Mockito.times(1))
                .save(Mockito.any(Task.class));
    }

    @Test
    void testUpdate_whenTaskDoesNotExists() {
        Long id = 1L;
        TaskDto taskDto = createTestTaskDto();

        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<TaskDto> updatedTaskDto = taskService.update(id, taskDto);

        assertThat(updatedTaskDto).isEmpty();

        Mockito.verify(taskRepository, Mockito.times(1))
                .findById(id);
        Mockito.verify(taskRepository, Mockito.never())
                .save(Mockito.any(Task.class));
    }

    @Test
    void testDelete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(id);
    }

    private TaskDto createTestTaskDto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return TaskDto.builder()
                .title("test title")
                .description("test description")
                .dueDate(LocalDateTime.parse("2025-06-10 17:50:00", formatter))
                .isCompleted(false)
                .userId(1L)
                .build();
    }
}
