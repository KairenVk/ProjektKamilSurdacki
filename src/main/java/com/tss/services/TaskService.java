package com.tss.services;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskListRepository taskListRepository;

    public Task addTask(Task newTask, TaskList list) {
        newTask.setTitle(HtmlUtils.htmlEscape(newTask.getTitle()));
        newTask.setDescription(HtmlUtils.htmlEscape(newTask.getDescription()));
        newTask.setTimeCreated(Timestamp.from(Instant.now()));
        newTask.setTaskList(list);
        taskRepository.save(newTask);
        return newTask;
    }

    public Task addTask(Task newTask) {
        newTask.setTitle(HtmlUtils.htmlEscape(newTask.getTitle()));
        newTask.setDescription(HtmlUtils.htmlEscape(newTask.getDescription()));
        newTask.setTimeCreated(Timestamp.from(Instant.now()));
        taskRepository.save(newTask);
        return newTask;
    }

    public Task editTask(Task task, Task modifiedTask) {
        if(modifiedTask.getTaskList() != null)
            task.setTaskList(modifiedTask.getTaskList());
        if(modifiedTask.getDescription() != null)
            task.setDescription(HtmlUtils.htmlEscape(modifiedTask.getDescription()));
        if(modifiedTask.getTitle() != null)
            task.setTitle(HtmlUtils.htmlEscape(modifiedTask.getTitle()));
        task.setTimeModified(Timestamp.from(Instant.now()));
        taskRepository.save(task);
        return task;
    }

    public void moveTask(Task task, Long taskListId) {
        task.setTaskList(taskListRepository.findById(taskListId)
                .orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), taskListId)));
        task.setTimeModified(Timestamp.from(Instant.now()));
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
