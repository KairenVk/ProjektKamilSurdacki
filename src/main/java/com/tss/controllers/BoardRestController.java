package com.tss.controllers;

import com.tss.assemblers.BoardModelAssembler;
import com.tss.entities.data.Board;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.BoardRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class BoardRestController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardModelAssembler boardModelAssembler;

    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/boards")
    public CollectionModel<EntityModel<Board>> all() {
        List<EntityModel<Board>> boards = boardRepository.findAll().stream()
                .map(boardModelAssembler::toModel)
                .toList();

        return CollectionModel.of(boards, linkTo(methodOn(BoardRestController.class).all()).withSelfRel());
    }

    @GetMapping("/boards/getByUserId/{userId}")
    public CollectionModel<EntityModel<Board>> getBoardsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), userId));
        List<EntityModel<Board>> boards = boardRepository.findAllByOwner(user).stream()
                .map(boardModelAssembler::toModel)
                .toList();

        return CollectionModel.of(boards, linkTo(methodOn(BoardRestController.class).getBoardsByUserId(userId)).withSelfRel());
    }
    @PostMapping("/addBoard")
    public EntityModel<Board> addBoard(@RequestBody Board board) {
        Board newBoard = boardService.addBoard(board);
        return boardModelAssembler.toModel(newBoard);
    }

    @GetMapping("/board/{boardId}")
    public EntityModel<Board> getBoard(@PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId));
        return boardModelAssembler.toModel(board);
    }

    @PutMapping("/board/{boardId}")
    public EntityModel<Board> editBoard(@RequestBody Board editParams, @PathVariable Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(),boardId));
        board = boardService.editBoard(board, editParams);
        return boardModelAssembler.toModel(board);
    }

    @DeleteMapping("/board/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException(Board.class.getSimpleName(), boardId)));
    }

    @PutMapping("/board/{boardId}/addBoardMembers")
    public EntityModel<Board> addBoardMembers(@RequestBody Collection<String> users, @PathVariable Long boardId) {
        Board board = boardService.addBoardMembersByUsernames(users, boardId);
        return boardModelAssembler.toModel(board);
    }

    //TODO: @GetMapping("/user/{id}/boards") place methods based on return type
}