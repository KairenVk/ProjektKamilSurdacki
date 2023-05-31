package com.tss.services;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    public TaskList addList(TaskList newTaskList) {
        newTaskList.setTime_created(Timestamp.from(Instant.now()));
        newTaskList.setList_order(taskListRepository.findAllByBoard(newTaskList.getBoard()).size()+1);
        taskListRepository.save(newTaskList);
        if (newTaskList.getTasks() != null) {
            for(Task task: newTaskList.getTasks()) {
                taskService.addTask(task, newTaskList);
            }
        }
        return newTaskList;
    }

    public TaskList editList(TaskList taskList, TaskList updatedTaskList) {
        taskList.setTitle(updatedTaskList.getTitle());
        taskList.setList_order(updatedTaskList.getList_order());
        taskList.setTime_modified(Timestamp.from(Instant.now()));
        taskListRepository.save(taskList);
        return taskList;
    }
}
