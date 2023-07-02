package com.tss.controllers;

import com.tss.assemblers.UserModelAssembler;
import com.tss.dto.UserDTO;
import com.tss.entities.data.Board;
import com.tss.entities.data.Task;
import com.tss.entities.data.TaskList;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityByNameNotFoundException;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.data.UserRepository;
import com.tss.services.AuthorizationService;
import com.tss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
@Transactional
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserModelAssembler userModelAssembler;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());
        for (EntityModel<User> user: users) {
            Collection<Board> boards = user.getContent().getOwnedBoards();
            for (Board board : boards) {
                Collection<TaskList> lists = board.getTaskLists();
                for (TaskList list: lists) {
                    list.getTasks().sort(Comparator.comparingInt(Task::getTaskOrder));;
                }
            }
        }
        return CollectionModel.of(users, linkTo(methodOn(UserRestController.class).all()).withSelfRel());
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(),id));
        Collection<Board> boards = user.getOwnedBoards();
        for (Board board: boards) {
            Collection<TaskList> lists = board.getTaskLists();
            for (TaskList list: lists) {
                list.getTasks().sort(Comparator.comparingInt(Task::getTaskOrder));
            }
        }
        return userModelAssembler.toModel(user);
    }

    @GetMapping("/user/getByUsername/{username}")
    public EntityModel<User> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityByNameNotFoundException(User.class.getSimpleName(), username));
        return userModelAssembler.toModel(user);
    }
    @PostMapping("/user/addUser")
    public EntityModel<User> addUser(@RequestBody UserDTO newUser) {
        User user = userService.addUser(newUser);
        return userModelAssembler.toModel(user);
    }

    @PutMapping("/user/{id}")
    public User editUser(@RequestBody UserDTO modifiedUser, @PathVariable Long id) {
        authorizationService.isAuthorized(id);
        return userService.editUser(modifiedUser,id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        authorizationService.isAuthorized(id);
        userService.deleteUser(id);
    }

}
