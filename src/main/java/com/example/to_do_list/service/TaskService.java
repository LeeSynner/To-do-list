package com.example.to_do_list.service;

import com.example.to_do_list.domain.Task;
import com.example.to_do_list.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    public List<TaskDto> getAllTasks() {
        return new ArrayList<>();
    }

    public TaskDto getTask(String id) {
        return new TaskDto("name", "description");
    }

    public TaskDto create(TaskDto taskDto) {
        Task task = new Task();

        return new TaskDto("Write pet project", "I need write pet project because i need money");
    }

    public TaskDto update(TaskDto task) {
        return task;
    }

    public TaskDto delete(TaskDto taskDto) {
        return taskDto;
    }
}
