package com.example.to_do_list.repository;

import com.example.to_do_list.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
