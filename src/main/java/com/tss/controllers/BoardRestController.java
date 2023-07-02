package com.tss.controllers;

import com.tss.assemblers.BoardModelAssembler;
import com.tss.entities.data.Board;
import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityByNameNotFoundException;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.AuthorizationService;
import com.tss.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
@Transactional
public class BoardRestController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardModelAssembler boardModelAssembler;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/boards")
    public CollectionModel<EntityModel<Board>> all() {
        List<EntityModel<Board>> boards = boardRepository.findAll().stream()
                .map(boardModelAssembler::toModel)
                .toList();
        sortTasksInBoards(boards);

        return CollectionModel.of(boards, linkTo(methodOn(BoardRestController.class).all()).withSelfRel());
    }

    @GetMapping("/boards/getByUserId/{userId}")
    public CollectionModel<EntityModel<Board>> getBoardsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), userId));
        List<EntityModel<Board>> boards = boardRepository.findAllByOwner(user).stream()
                .map(boardModelAssembler::toModel)
                .toList();
        sortTasksInBoards(boards);

        return CollectionModel.of(boards, linkTo(methodOn(BoardRestController.class).getBoardsByUserId(userId)).withSelfRel());
    }

    @GetMapping("/boards/getByUsername/{username}")
    public CollectionModel<EntityModel<Board>> getBoardsByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityByNameNotFoundException(User.class.getSimpleName(), username));
        List<EntityModel<Board>> boards = boardRepository.findAllByOwner(user).stream()
                .map(boardModelAssembler::toModel)
                .toList();
        sortTasksInBoards(boards);

        return CollectionModel.of(boards, linkTo(methodOn(BoardRestController.class).getBoardsByUsername(username)).withSelfRel());
    }

    @PostMapping("/board/addBoard")
    public EntityModel<Board> addBoard(@RequestBody Board board) {
        Board newBoard = boardService.restAddBoard(board);
        return boardModelAssembler.toModel(newBoard);
    }

    @GetMapping("/board/{boardId}")
    public EntityModel<Board> getBoard(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId));
        sortTasksInBoard(board);
        return boardModelAssembler.toModel(board);
    }

    @PutMapping("/board/{boardId}")
    public EntityModel<Board> editBoard(@RequestBody Board editParams, @PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId));
        authorizationService.isAuthorized(board.getOwner().getId());
        board = boardService.editBoard(board, editParams);
        return boardModelAssembler.toModel(board);
    }

    @DeleteMapping("/board/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        Board boardToDelete = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        authorizationService.isAuthorized(boardToDelete.getOwner().getId());
        boardService.deleteBoard(boardToDelete);
    }

    @PutMapping("/board/{boardId}/addBoardMembers")
    public EntityModel<Board> addBoardMembers(@RequestBody Collection<String> users, @PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId));
        authorizationService.isAuthorized(board.getOwner().getId());
        board = boardService.addBoardMembersByUsernames(users, boardId);
        return boardModelAssembler.toModel(board);
    }

    private static void sortTasksInBoards(List<EntityModel<Board>> boards) {
        for (EntityModel<Board> board : boards) {
            Collection<TaskList> lists = board.getContent().getTaskLists();
            for (TaskList list : lists) {
                list.getTasks().sort(Comparator.comparingInt(Task::getTaskOrder));
            }
        }
    }

    private static void sortTasksInBoard(Board board) {
            Collection<TaskList> lists = board.getTaskLists();
            for (TaskList list : lists) {
                list.getTasks().sort(Comparator.comparingInt(Task::getTaskOrder));
            }
    }
}
