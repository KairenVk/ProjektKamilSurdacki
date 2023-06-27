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
import java.util.Objects;

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

    public Task editTask(Task task, Task modifiedTask) {
        Long newListId = modifiedTask.getTaskList().getId();
        TaskList newList = taskListRepository.findById(newListId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), newListId));
        if(modifiedTask.getDescription() != null)
            task.setDescription(HtmlUtils.htmlEscape(modifiedTask.getDescription()));
        if(modifiedTask.getTitle() != null)
            task.setTitle(HtmlUtils.htmlEscape(modifiedTask.getTitle()));
        if(!Objects.equals(task.getTaskList().getId(), modifiedTask.getTaskList().getId()) && !Objects.equals(task.getTaskOrder(), modifiedTask.getTaskOrder())) {
            decrementTaskOrders(task.getTaskList(), task.getTaskOrder());
            incrementTaskOrders(newList, modifiedTask.getTaskOrder());
            task.setTaskList(newList);
            task.setTaskOrder(modifiedTask.getTaskOrder());
        }
        else if(!Objects.equals(task.getTaskList().getId(), modifiedTask.getTaskList().getId()) && Objects.equals(task.getTaskOrder(), modifiedTask.getTaskOrder())) {
            TaskList currentList = task.getTaskList();
            decrementTaskOrders(currentList, task.getTaskOrder());
            incrementTaskOrders(newList, task.getTaskOrder());
            task.setTaskList(newList);
        }
        else if(!Objects.equals(task.getTaskOrder(), modifiedTask.getTaskOrder())) {
            updateTaskOrder(task, modifiedTask.getTaskOrder(), task.getTaskOrder() > modifiedTask.getTaskOrder());
        }
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

    private void decrementTaskOrders(TaskList list, Integer taskOrder) {
        List<Task> tasksToUpdate = taskRepository.findAllByTaskOrderGreaterThanAndTaskList(taskOrder, list);

        for (Task taskToUpdate: tasksToUpdate) {
                taskToUpdate.decrementTaskOrder();
        }
    }

    private void incrementTaskOrders(TaskList list, Integer taskOrder) {
        List<Task> tasksToUpdate = taskRepository.findAllByTaskOrderGreaterThanEqualAndTaskList(taskOrder, list);

        for (Task taskToUpdate: tasksToUpdate) {
            taskToUpdate.incrementTaskOrder();
        }
    }

    private void updateTaskOrder(Task task, Integer newTaskOrder, boolean increment) {
        List<Task> tasksToUpdate;
        if (increment) {
            tasksToUpdate = taskRepository.findAllByTaskOrderLessThanAndTaskOrderGreaterThanEqualAndTaskList(task.getTaskOrder(), newTaskOrder, task.getTaskList());
        }
        else {
            tasksToUpdate = taskRepository.findAllByTaskOrderGreaterThanAndTaskOrderLessThanEqualAndTaskList(task.getTaskOrder(), newTaskOrder, task.getTaskList());
        }

        for (Task taskToUpdate: tasksToUpdate) {
            if (increment)
                taskToUpdate.incrementTaskOrder();
            else
                taskToUpdate.decrementTaskOrder();
        }
        task.setTaskOrder(newTaskOrder);
    }

    public Task editTaskDebug(Task task, Task newTask) {
        task.setTaskOrder(newTask.getTaskOrder());
        taskRepository.save(task);
        return task;
    }
}
