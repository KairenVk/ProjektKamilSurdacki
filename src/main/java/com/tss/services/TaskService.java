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
import java.util.Collection;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    TaskListRepository taskListRepository;

    public void addTaskListObject(Task newTask, TaskList list) {
        newTask.setTaskList(list);
        addTask(newTask);
    }

    public Task addTaskListId(Task newTask, Long listId) {
        newTask.setTaskList(taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId)));
        return addTask(newTask);
    }

    public Task addTask(Task newTask) {
        newTask.setTitle(HtmlUtils.htmlEscape(newTask.getTitle()));
        newTask.setDescription(HtmlUtils.htmlEscape(newTask.getDescription()));
        newTask.setTimeCreated(Timestamp.from(Instant.now()));
        newTask.setTaskOrder(taskRepository.findAllByTaskList(newTask.getTaskList()).size());
        taskRepository.save(newTask);
        return newTask;
    }

    public Task editTask(Task task, Task newTask) {
        if(newTask.getTaskList() != null) {
            Long newListId = newTask.getTaskList().getId();
            TaskList currentList = task.getTaskList();
            TaskList newList = taskListRepository.findById(newListId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), newListId));
            task.setTaskList(null);
            updateTaskOrder(currentList, task.getTaskOrder(),false);
            updateTaskOrder(newList, task.getTaskOrder(),true);
            task.setTaskList(newList);
        }
        if(newTask.getDescription() != null)
            task.setDescription(HtmlUtils.htmlEscape(newTask.getDescription()));
        if(newTask.getTitle() != null)
            task.setTitle(HtmlUtils.htmlEscape(newTask.getTitle()));
//        if(newTask.getTaskOrder() != null) {
//            if (task.getTaskOrder() < newTask.getTaskOrder())
//                updateTaskOrder(task, newTask.getTaskOrder(), false);
//            else
//                updateTaskOrder(task, newTask.getTaskOrder(), true);
//        }
        task.setTimeModified(Timestamp.from(Instant.now()));
        taskRepository.save(task);
        return task;
    }

    public void deleteTask(Task taskToDelete) {
        Collection<Task> tasksToChangeOrder = taskRepository.findAllByTaskOrderGreaterThanAndTaskList(taskToDelete.getTaskOrder(), taskToDelete.getTaskList());
        for (Task task: tasksToChangeOrder) {
            task.decrementTaskOrder();
        }
        taskRepository.delete(taskToDelete);
    }

    private void updateTaskOrder(TaskList list, Integer taskOrder, boolean increment) {
        List<Task> tasksToUpdate = taskRepository.findAllByTaskOrderGreaterThanAndTaskList(taskOrder, list);
        for (Task taskToUpdate: tasksToUpdate) {
            if (increment)
                taskToUpdate.incrementTaskOrder();
            else
                taskToUpdate.decrementTaskOrder();
        }
    }

    private void updateTaskOrder(Task task, Integer newTaskOrder, boolean increment) {
        task.setTaskOrder(-1);
        if (increment) {
            List<Task> tasksToUpdate = taskRepository.findAllByTaskOrderLessThanAndTaskOrderGreaterThanEqualAndTaskList(task.getTaskOrder(), newTaskOrder, task.getTaskList());
            for (Task taskToUpdate: tasksToUpdate) {
                taskToUpdate.incrementTaskOrder();
            }
        }
        else {
            List<Task> tasksToUpdate = taskRepository.findAllByTaskOrderGreaterThanAndTaskOrderLessThanEqualAndTaskList(task.getTaskOrder(), newTaskOrder, task.getTaskList());
            for (Task taskToUpdate: tasksToUpdate) {
                taskToUpdate.decrementTaskOrder();
            }
        }
        task.setTaskOrder(newTaskOrder);
    }

    public Task editTaskDebug(Task task, Task newTask) {
        task.setTaskOrder(newTask.getTaskOrder());
        taskRepository.save(task);
        return task;
    }
}
