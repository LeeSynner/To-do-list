package com.example.to_do_list.service.interfaces;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.domain.User;
import com.example.to_do_list.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ITaskService {
    List<TaskDto> getByUsername(String username);

    Optional<TaskDto> getById(Long id);

    TaskDto create(TaskDto taskDto);

    Optional<TaskDto> update(Long id, TaskDto taskDto);

    void delete(Long id);

    default TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .isCompleted(task.isCompleted())
                .userId(task.getUser() != null ? task.getUser().getId() : 0)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt()).build();
    }

    default Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .isCompleted(taskDto.isCompleted())
                .user(User.builder().id(taskDto.getUserId()).build())
                .createdAt(LocalDateTime.now())
                .updatedAt(null).build();
    }

}
