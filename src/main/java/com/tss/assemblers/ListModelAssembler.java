package com.tss.assemblers;

import com.tss.controllers.BoardRestController;
import com.tss.controllers.ListRestController;
import com.tss.entities.data.TaskList;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

@Component
public class ListModelAssembler {
    public EntityModel<TaskList> toModel(TaskList taskList) {
        return EntityModel.of(taskList,
                linkTo(methodOn(BoardRestController.class).getBoard(taskList.getBoard().getId())).withRel("board"),
                linkTo(methodOn(ListRestController.class).getList(taskList.getId())).withSelfRel(),
                linkTo(methodOn(ListRestController.class).all(taskList.getBoard().getId())).withRel("lists"));
    }
}
