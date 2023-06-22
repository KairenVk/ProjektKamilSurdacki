package com.tss.repositories.data;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    public List<Task> findAllByTaskList(TaskList taskList);

    List<Task> findAllByTaskOrderGreaterThanAndTaskOrderLessThanEqualAndTaskList(Integer order1, Integer order2, TaskList list);
    List<Task> findAllByTaskOrderLessThanAndTaskOrderGreaterThanEqualAndTaskList(Integer order1, Integer order2, TaskList list);

    List<Task> findAllByTaskOrderGreaterThanAndTaskList(Integer order, TaskList list);
}