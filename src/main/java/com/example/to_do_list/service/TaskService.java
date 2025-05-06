package com.example.to_do_list.service;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<TaskDto> getById(Long id) {
        return taskRepository.findById(id).map(this::toDto);
    }

    public TaskDto create(TaskDto taskDto) {
        Task task = toEntity(taskDto);
        return toDto(taskRepository.save(task));
    }

    public Optional<TaskDto> update(Long id, TaskDto taskDto) {
        Optional<Task> task = taskRepository.findById(id);
        task.ifPresent(t -> {
            t.setTitle(taskDto.getTitle());
            t.setDescription(taskDto.getDescription());
            t.setDueDate(taskDto.getDueDate());
            t.setCompleted(taskDto.isCompleted());
            t.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(t);
        });
        return task.map(this::toDto);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public TaskDto toDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .isCompleted(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt()).build();
    }

    public Task toEntity(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .isCompleted(taskDto.isCompleted())
                .createdAt(LocalDateTime.now())
                .updatedAt(null).build();
    }

}