package com.tss.controllers;

import com.tss.assemblers.UserModelAssembler;
import com.tss.entities.RestForm;
import com.tss.entities.data.User;
import com.tss.exceptions.UserNotFoundException;
import com.tss.repositories.credentials.CredentialsRepository;
import com.tss.repositories.data.UserRepository;
import com.tss.services.CredentialsService;
import com.tss.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
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
                .orElseThrow(() -> new UserNotFoundException(id));
        return userModelAssembler.toModel(user);
    }

    @PostMapping("/addUser")
    User addUser(@RequestBody RestForm newForm) {
        User user = userService.addUser(newForm);
        credentialsService.addCredentials(newForm, user.getId());
        return user;
    }

    @PutMapping("/editUser/{id}")
    User editUser(@RequestBody RestForm editForm, @PathVariable Long id) {
        User user = userService.editUser(editForm,id);
        credentialsService.editCredentials(editForm,id);
        return user;
    }

    @DeleteMapping("/employees/{id}")
    void deleteUser(@PathVariable Long id) {
        userRepository.delete(userRepository.getReferenceById(id));
    }

}
