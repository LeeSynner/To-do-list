package com.example.to_do_list.service;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.repository.TaskRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<TaskDto> getTaskById(Long id) {
        return taskRepository.findById(id).map(this::toDto);
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
