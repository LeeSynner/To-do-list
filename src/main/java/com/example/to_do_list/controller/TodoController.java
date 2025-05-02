package com.example.to_do_list.controller;


import com.example.to_do_list.dto.TaskDto;
import com.example.to_do_list.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

    private final TaskService taskService;

    public TodoController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/task")
    public ResponseEntity<TaskDto> index() {
        System.out.println("Enter to index");
        TaskDto task = new TaskDto("Homework", "I need todo homework before Friday");
        return ResponseEntity.ok(task);
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        taskService.create(taskDto);
    }
}
