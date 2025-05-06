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
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setIsCompleted(taskDto.getIsCompleted());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(null);
        return toDto(taskRepository.save(task));
    }

    public TaskDto toDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setIsCompleted(task.getIsCompleted());
        taskDto.setCreatedAt(task.getCreatedAt());
        taskDto.setUpdatedAt(task.getUpdatedAt());
        return taskDto;
    }

}