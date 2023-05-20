package com.tss.services;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskListRepository taskListRepository;

    public Task addTask(Task newTask, TaskList list) {
        newTask.setTime_created(Timestamp.from(Instant.now()));
        newTask.setTaskList(list);
        taskRepository.save(newTask);
        return newTask;
    }

    public Task addTask(Task newTask) {
        newTask.setTime_created(Timestamp.from(Instant.now()));
        taskRepository.save(newTask);
        return newTask;
    }

    public Task editTask(Task task, Task modifiedTask) {
        if(modifiedTask.getTaskList() != null)
            task.setTaskList(modifiedTask.getTaskList());
        if(modifiedTask.getDescription() != null)
            task.setDescription(modifiedTask.getDescription());
        if(modifiedTask.getTitle() != null)
            task.setTitle(modifiedTask.getTitle());
        task.setTime_modified(Timestamp.from(Instant.now()));
        taskRepository.save(task);
        return task;
    }

    public void moveTask(Task task, Long taskListId) {
        task.setTaskList(taskListRepository.findById(taskListId)
                .orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), taskListId)));
        task.setTime_modified(Timestamp.from(Instant.now()));
    }
}