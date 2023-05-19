package com.tss.controllers;

import com.tss.assemblers.ListModelAssembler;
import com.tss.entities.data.Board;
import com.tss.entities.data.TaskList;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.TaskListRepository;
import com.tss.services.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class ListRestController {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ListModelAssembler listModelAssembler;


    @GetMapping("/lists/getListsByBoard/{boardId}")
    public CollectionModel<EntityModel<TaskList>> all(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId));
        java.util.List<EntityModel<TaskList>> lists = taskListRepository.findAllByBoard(board).stream()
                .map(listModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(lists, linkTo(methodOn(ListRestController.class).all(boardId)).withSelfRel());
    }

    @GetMapping("/list/{listId}")
    public EntityModel<TaskList> getList(@PathVariable Long listId) {
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId));
        return listModelAssembler.toModel(taskList);
    }

    @PutMapping("/list/{listId}")
    public EntityModel<TaskList> editList(@RequestBody TaskList editedTaskList, @PathVariable Long listId) {
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId));
        taskList = taskListService.editList(taskList, editedTaskList);
        return listModelAssembler.toModel(taskList);
    }

    @DeleteMapping("/list/{listId}")
    public void deleteList(@PathVariable("listId") Long listId) {
        TaskList taskList = taskListRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException(TaskList.class.getSimpleName(), listId));
        taskListRepository.delete(taskList);
    }

    @PostMapping("/board/{boardId}/addList")
    public EntityModel<TaskList> addList(@PathVariable Long boardId, @RequestBody TaskList newTaskList) {
        newTaskList.setBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId)));
        TaskList taskList = taskListService.addList(newTaskList);
        return listModelAssembler.toModel(taskList);
    }


}
