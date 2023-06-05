package com.tss.services;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    public TaskList addList(TaskList newTaskList) {
        newTaskList.setTimeCreated(Timestamp.from(Instant.now()));
        newTaskList.setListOrder(taskListRepository.findAllByBoard(newTaskList.getBoard()).size());
        taskListRepository.save(newTaskList);
        if (newTaskList.getTasks() != null) {
            for(Task task: newTaskList.getTasks()) {
                taskService.addTask(task, newTaskList);
            }
        }
        return newTaskList;
    }

    public TaskList editList(TaskList taskList, TaskList updatedTaskList) {
        if (updatedTaskList.getTitle() != null) {
            taskList.setTitle(updatedTaskList.getTitle());
        }
        if (updatedTaskList.getListOrder() != null) {
            Integer oldPos = taskList.getListOrder();
            Integer newPos = updatedTaskList.getListOrder();
            System.out.println(taskList.getId());
            if (oldPos > newPos) {
                List<TaskList> listsInBetween= taskListRepository.findAllByListOrderLessThanAndListOrderGreaterThanEqual(oldPos, newPos);
                for (TaskList list: listsInBetween) {
                    System.out.println(list.getTitle());
                    list.incrementList_order();
                }
            }
            else {
                List<TaskList> listsInBetween= taskListRepository.findAllByListOrderGreaterThanAndListOrderLessThanEqual(oldPos, newPos);
                for (TaskList list: listsInBetween) {
                    System.out.println(list.getTitle());
                    list.decrementList_order();
                }
            }
            taskList.setListOrder(updatedTaskList.getListOrder());
        }
        taskList.setTimeModified(Timestamp.from(Instant.now()));
        taskListRepository.save(taskList);
        return taskList;
    }
}
