package com.tss.controllers;

import com.tss.assemblers.UserModelAssembler;
import com.tss.to.UserDTO;
import com.tss.entities.data.User;
import com.tss.exceptions.EntityByNameNotFoundException;
import com.tss.exceptions.EntityNotFoundException;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.CredentialsService;
import com.tss.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rest")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserModelAssembler userModelAssembler;

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users, linkTo(methodOn(UserRestController.class).all()).withSelfRel());
    }

    @GetMapping("/user/{id}")
    public EntityModel<User> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(),id));
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
        return userService.editUser(modifiedUser,id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
