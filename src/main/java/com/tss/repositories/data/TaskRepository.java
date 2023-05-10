package com.tss.repositories.data;

import com.tss.entities.data.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}