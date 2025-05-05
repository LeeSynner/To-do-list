package com.example.to_do_list.controller;

import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TodoController {
    private final TaskService taskService;

    public TodoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasks() {
        System.out.println("Enter to index");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        Optional<TaskDto> taskDto = taskService.getTaskById(id);
        return taskDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
