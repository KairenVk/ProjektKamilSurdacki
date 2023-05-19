package com.tss.assemblers;

import com.tss.controllers.UserRestController;
import com.tss.entities.RestForm;
import com.tss.entities.data.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserRestController.class).getUser(user.getId())).withSelfRel(),
                linkTo(methodOn(UserRestController.class).all()).withRel("users"));
    }
}
