package com.example.to_do_list.service;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.repository.TaskRepository;
import com.example.to_do_list.service.interfaces.ITaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDto> getByUsername(String username) {
        return taskRepository.findByUserUsername(username).stream().map(this::toDto).toList();
    }

    @Override
    public Optional<TaskDto> getById(Long id) {
        return taskRepository.findById(id).map(this::toDto);
    }

    @Override
    public TaskDto create(TaskDto taskDto) {
        Task task = toEntity(taskDto);
        return toDto(taskRepository.save(task));
    }

    @Override
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

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}