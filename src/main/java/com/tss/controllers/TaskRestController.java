package com.tss.controllers;

import com.tss.assemblers.TaskModelAssembler;
import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.TaskListRepository;
import com.tss.repositories.data.TaskRepository;
import com.tss.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class TaskRestController {
    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskModelAssembler taskModelAssembler;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks/getTasksByList/{listId}")
    public CollectionModel<EntityModel<Task>> all(@PathVariable Long listId) {
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId));
        List<EntityModel<Task>> tasks = taskRepository.findAllByTaskList(taskList).stream()
                .map(taskModelAssembler::toModel).sorted(Comparator.comparingInt(o -> o.getContent().getTaskOrder())).collect(Collectors.toList());
        return CollectionModel.of(tasks, linkTo(methodOn(TaskRestController.class).all(listId)).withSelfRel());
    }

    @PostMapping("/list/{listId}/addTask")
    public EntityModel<Task> addTask(@RequestBody Task newTask, @PathVariable Long listId) {
        Task task = taskService.addTaskListId(newTask, listId);
        return taskModelAssembler.toModel(task);
    }

    @GetMapping("/task/{taskId}")
    public EntityModel<Task> getTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class.getSimpleName(), taskId));
        return taskModelAssembler.toModel(task);
    }

    @PutMapping("/task/{taskId}")
    public EntityModel<Task> editTask(@PathVariable Long taskId, @RequestBody Task modifiedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class.getSimpleName(), taskId));
        task = taskService.editTask(task, modifiedTask);
        return taskModelAssembler.toModel(task);
    }

    @PutMapping("/task/debug/{taskId}")
    public EntityModel<Task> editTaskDebug(@PathVariable Long taskId, @RequestBody Task modifiedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Task.class.getSimpleName(), taskId));
        task = taskService.editTaskDebug(task, modifiedTask);
        return taskModelAssembler.toModel(task);
    }

    @DeleteMapping("/task/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        Task taskToDelete = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException(Task.class.getSimpleName(), taskId));
        taskService.deleteTask(taskToDelete);
    }
}
