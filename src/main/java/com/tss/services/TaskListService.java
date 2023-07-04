package com.tss.services;

import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.MissingParameterException;
import com.tss.repositories.data.TaskListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class TaskListService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskService taskService;

    public TaskList addList(TaskList newTaskList) {
        if(newTaskList.getTitle() == null || newTaskList.getTitle().isEmpty())
            throw new MissingParameterException("title");
        newTaskList.setTitle(HtmlUtils.htmlEscape(newTaskList.getTitle()));
        newTaskList.setTimeCreated(Timestamp.from(Instant.now()));
        newTaskList.setListOrder(taskListRepository.findAllByBoard(newTaskList.getBoard()).size());
        taskListRepository.save(newTaskList);
        if (newTaskList.getTasks() != null) {
            for(Task task: newTaskList.getTasks()) {
                taskService.addTaskListObject(task, newTaskList);
            }
        }
        return newTaskList;
    }

    public TaskList editList(TaskList taskList, TaskList updatedTaskList) {
        if (updatedTaskList.getTitle() != null && !updatedTaskList.getTitle().isEmpty()) {
            taskList.setTitle(HtmlUtils.htmlEscape(updatedTaskList.getTitle()));
        }
        if (updatedTaskList.getListOrder() != null) {
            Integer oldPos = taskList.getListOrder();
            Integer newPos = updatedTaskList.getListOrder();
            if (oldPos > newPos) {
                List<TaskList> listsInBetween= taskListRepository.findAllByListOrderLessThanAndListOrderGreaterThanEqualAndBoard(oldPos, newPos, taskList.getBoard());
                for (TaskList list: listsInBetween) {
                    list.incrementList_order();
                }
            }
            else {
                List<TaskList> listsInBetween= taskListRepository.findAllByListOrderGreaterThanAndListOrderLessThanEqualAndBoard(oldPos, newPos, taskList.getBoard());
                for (TaskList list: listsInBetween) {
                    list.decrementList_order();
                }
            }
            taskList.setListOrder(updatedTaskList.getListOrder());
        }
        taskList.setTimeModified(Timestamp.from(Instant.now()));
        taskListRepository.save(taskList);
        return taskList;
    }

    public void deleteList(TaskList listToDelete) {
        List<TaskList> listsToChangeOrder = taskListRepository.findAllByListOrderGreaterThanAndBoard(listToDelete.getListOrder(), listToDelete.getBoard());
        for (TaskList list: listsToChangeOrder) {
            list.decrementList_order();
        }
        taskListRepository.delete(listToDelete);
    }
}
